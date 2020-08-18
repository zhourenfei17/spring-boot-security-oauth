package com.jackeroo.oauth.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.jackeroo.oauth.common.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对oauth接口成功返回的结果进行封装，保证结果格式的一致性
 * @author alex
 * @date 2020/07/30
 */
@ControllerAdvice(basePackages = "org.springframework.security.oauth2.provider.endpoint")
public class ResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 只对oauth提供的相关接口返回的Json数据进行处理
        if(request.getURI().getPath().startsWith("/oauth") && selectedContentType.includes(MediaType.APPLICATION_JSON)){
            // 排除异常处理中已经处理过的json结果
            if(!JSON.toJSONString(body).contains("code")){
                return Result.ok(body);
            }
        }
        return body;
    }
}
