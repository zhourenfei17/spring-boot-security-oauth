package com.jackeroo.oauth.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码错误异常
 * @author alex
 * @date 2020/08/10
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
