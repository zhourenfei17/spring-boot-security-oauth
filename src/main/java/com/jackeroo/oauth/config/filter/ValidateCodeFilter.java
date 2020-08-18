package com.jackeroo.oauth.config.filter;

import com.jackeroo.oauth.common.utils.MD5Util;
import com.jackeroo.oauth.config.exception.ValidateCodeException;
import com.jackeroo.oauth.config.handler.SecurityLoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security登录验证码拦截处理
 * @author alex
 * @date 2020/08/10
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityLoginFailureHandler securityLoginFailureHandler;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/doLogin") && request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())){
            try {
                validate(request);
            }catch (ValidateCodeException e){
                securityLoginFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 通过的情况下，继续执行其他过滤器链
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String code = ServletRequestUtils.getStringParameter(request, "code");
        String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp");

        String realKey = MD5Util.MD5Encode(code.toLowerCase() + timestamp, "utf-8");

        String serverCode = redisTemplate.opsForValue().get(realKey);
        redisTemplate.delete(realKey);

        if(serverCode == null || !serverCode.equalsIgnoreCase(code)){
            throw new ValidateCodeException("验证码不正确");
        }
    }
}
