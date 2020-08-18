package com.jackeroo.oauth.config.serializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author alex
 * @date 2020/07/29
 */
public class OAuth2AuthenticationSerializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type == OAuth2Authentication.class) {
            try {
                Object o = parse(parser);
                if (o == null) {
                    return null;
                } else if (o instanceof OAuth2Authentication) {
                    return (T) o;
                }

                JSONObject jsonObject = (JSONObject) o;
                OAuth2Request request = parseOAuth2Request(jsonObject);

                //判断json节点userAuthentication的类型，根据类型动态取值
                //UsernamePasswordAuthenticationToken 密码模式/授权码模式下，存储类型为UsernamePasswordAuthenticationToken
                //PreAuthenticatedAuthenticationToken 刷新token模式下，存储类型为PreAuthenticatedAuthenticationToken
                Object autoType = jsonObject.get("userAuthentication");
                return (T) new OAuth2Authentication(request, jsonObject.getObject("userAuthentication", (Type) autoType.getClass()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private OAuth2Request parseOAuth2Request(JSONObject jsonObject) {
        JSONObject json = jsonObject.getObject("oAuth2Request", JSONObject.class);
        Map<String, String> requestParameters = json.getObject("requestParameters", Map.class);
        String clientId = json.getString("clientId");
        String grantType = json.getString("grantType");
        String redirectUri = json.getString("redirectUri");
        Boolean approved = json.getBoolean("approved");
        Set<String> responseTypes = json
                .getObject("responseTypes", new TypeReference<HashSet<String>>() {
                });
        Set<String> scope = json.getObject("scope", new TypeReference<HashSet<String>>() {
        });
        Set<String> authorities = json.getObject("authorities", new TypeReference<HashSet<String>>() {
        });
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(0);
        if (authorities != null && !authorities.isEmpty()) {
            authorities.forEach(s -> grantedAuthorities.add(new SimpleGrantedAuthority(s)));
        }
        Set<String> resourceIds = json
                .getObject("resourceIds", new TypeReference<HashSet<String>>() {
                });
        Map<String, Serializable> extensions = json
                .getObject("extensions", new TypeReference<HashMap<String, Serializable>>() {
                });

        OAuth2Request request = new OAuth2Request(requestParameters, clientId,
                grantedAuthorities, approved, scope, resourceIds, redirectUri, responseTypes, extensions);
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scope, grantType);
        request.refresh(tokenRequest);
        return request;
    }

    private Object parse(DefaultJSONParser parse) {
        JSONObject object = new JSONObject(parse.lexer.isEnabled(Feature.OrderedField));
        Object parsedObject = parse.parseObject((Map) object);
        if (parsedObject instanceof JSONObject) {
            return (JSONObject) parsedObject;
        } else if (parsedObject instanceof OAuth2Authentication) {
            return parsedObject;
        } else {
            return parsedObject == null ? null : new JSONObject((Map) parsedObject);
        }
    }
}
