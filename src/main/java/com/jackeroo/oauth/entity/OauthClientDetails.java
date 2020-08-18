package com.jackeroo.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author alex
 * @date 2020/07/21
 */
@TableName("oauth_client_details")
@Data
public class OauthClientDetails implements Serializable {
    @TableId
    private String id;
    /**应用名称*/
    private String appName;
    /**应用key*/
    private String appKey;
    /**应用密钥*/
    private String appSecret;
    /**资源id集合*/
    private String resourceIds;
    /**oauth权限范围*/
    private String scope;
    /**支持的授权类型*/
    private String authorizedGrantTypes;
    /**重定向uri*/
    private String redirectUri;
    /**security权限值*/
    private String authorities;
    /**access_token有效时间*/
    private Integer accessTokenValidity;
    /**refresh_token有效时间*/
    private Integer refreshTokenValidity;
    /**客户端的其他信息，必须为json格式*/
    private String additionalInformation;
    /**是否已存档*/
    private Integer archived;
    /**是否是信任的,0:不受信任，1：信任的*/
    private Integer trusted;
    /**是否自动跳过approve操作，默认为false，取值范围：true,false,read,write*/
    private String autoapprove;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    /**更新人*/
    private String updateBy;
    /**更新时间*/
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

}
