package com.jackeroo.oauth.config.handler;

import com.jackeroo.oauth.config.exception.ValidateCodeException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security登录失败处理器
 * @author alex
 * @date 2020/08/10
 */
@Component
public class SecurityLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof ValidateCodeException){
            response.sendRedirect("/login?error=codeError");
        }else{
            response.sendRedirect("/login?error=1");
        }
    }
}
