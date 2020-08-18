package com.jackeroo.oauth.config.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jackeroo.oauth.adapter.UserDetailsAdapter;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * fastjson redis存储json格式序列化反序列化工具类
 * @author alex
 * @date 2020/07/29
 */
public class FastJsonRedisTokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {

    private final static ParserConfig defaultRedisConfig = new ParserConfig();

    static {
        // 自定义oauth2序列化，
        defaultRedisConfig.setAutoTypeSupport(true);
        ////自定义DefaultOauth2RefreshTokenSerializer反序列化
        defaultRedisConfig.putDeserializer(DefaultOAuth2RefreshToken.class, new DefaultOauth2RefreshTokenSerializer());
        ////自定义OAuth2Authentication反序列化
        defaultRedisConfig.putDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationSerializer());
        //添加autotype白名单
        defaultRedisConfig.addAccept("org.springframework.security.oauth2.provider.");
        defaultRedisConfig.addAccept("org.springframework.security.oauth2.provider.client");
        TypeUtils.addMapping("org.springframework.security.oauth2.provider.OAuth2Authentication",
                OAuth2Authentication.class);
        TypeUtils.addMapping("org.springframework.security.oauth2.provider.client.BaseClientDetails",
                BaseClientDetails.class);

        defaultRedisConfig.addAccept("org.springframework.security.oauth2.common.");
        TypeUtils.addMapping("org.springframework.security.oauth2.common.DefaultOAuth2AccessToken", DefaultOAuth2AccessToken.class);
        TypeUtils.addMapping("org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken", DefaultExpiringOAuth2RefreshToken.class);

        defaultRedisConfig.addAccept("com.pthin.oauth.server.adapter");
        TypeUtils.addMapping("com.pthin.oauth.server.adapter.UserDetailsAdapter", UserDetailsAdapter.class);

        defaultRedisConfig.addAccept("org.springframework.security.web.authentication.preauth");
        TypeUtils.addMapping("org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken", PreAuthenticatedAuthenticationToken.class);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Assert.notNull(clazz, "clazz can't be null");
        if(bytes == null || bytes.length == 0){
            return null;
        }
        try {
            return JSON.parseObject(new String(bytes, IOUtils.UTF8), clazz, defaultRedisConfig);
        }catch (Exception e){
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public String deserializeString(byte[] bytes) {
        if(bytes == null || bytes.length == 0){
            return null;
        }
        return new String(bytes, IOUtils.UTF8);
    }

    @Override
    public byte[] serialize(Object object) {
        if(object == null){
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(object, SerializerFeature.WriteClassName,
                    SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e){
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] serialize(String data) {
        if(StringUtils.isEmpty(data)){
            return new byte[0];
        }
        return data.getBytes(IOUtils.UTF8);
    }
}
