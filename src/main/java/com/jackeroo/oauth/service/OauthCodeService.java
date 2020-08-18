package com.jackeroo.oauth.service;

import com.jackeroo.oauth.entity.OauthCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 授权码模式的code处理，使用redis存储
 * @author alex
 * @date 2020/07/23
 */
@Service
public class OauthCodeService implements AuthorizationCodeServices {

    /**
     * 使用redis存储代替数据库存储
     */
    @Resource
    RedisTemplate<String, OauthCode> redisTemplate;

    // 授权码默认为6位，此处更改为10位长度
    private static RandomValueStringGenerator generator = new RandomValueStringGenerator(10);

    private static final String prefix = "AuthorizationCode:";

    /**
     * 重写生成code方法
     * @param authentication
     * @return
     */
    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = generator.generate();
        store(code, authentication);
        return code;
    }

    /**
     * 消费code
     * @param code
     * @return
     * @throws InvalidGrantException
     */
    @Override
    public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
        OAuth2Authentication auth = this.remove(code);
        if (auth == null) {
            throw new InvalidGrantException("Invalid authorization code: " + code);
        }
        return auth;
    }

    /**
     * 将code保存到数据库
     * @param code
     * @param authentication
     */
    protected void store(String code, OAuth2Authentication authentication) {
        OauthCode oauthCode = new OauthCode();
        oauthCode.setCode(code);
        oauthCode.setAuthentication(SerializationUtils.serialize(authentication));

        redisTemplate.opsForValue().set(prefix + code, oauthCode, 5, TimeUnit.MINUTES);
    }

    /**
     * 删除code
     * @param code
     * @return
     */
    protected OAuth2Authentication remove(String code) {
        OauthCode oauthCode = redisTemplate.opsForValue().get(prefix + code);
        if(oauthCode != null){
            redisTemplate.delete(prefix + code);

            return SerializationUtils.deserialize(oauthCode.getAuthentication());
        }
        return null;
    }
}
