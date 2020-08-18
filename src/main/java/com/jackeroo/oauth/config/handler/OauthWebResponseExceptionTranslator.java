package com.jackeroo.oauth.config.handler;

import com.jackeroo.oauth.common.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * oauth认证失败返回结果处理
 * @author alex
 * @date 2020/07/23
 */
@Component
public class OauthWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
    @Override
    public ResponseEntity translate(Exception e) {
        e.printStackTrace();
        OAuth2Exception oauth = (OAuth2Exception) e;
        ResponseEntity response = new ResponseEntity(Result.error(oauth.getMessage()), HttpStatus.OK);
        return response;
    }
}
