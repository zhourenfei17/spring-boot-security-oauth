package com.jackeroo.oauth.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alex
 * @date 2020/07/23
 */
@Data
public class OauthCode implements Serializable {
    private static final long serialVersionUID = -1326285297837903604L;

    private String code;
    private byte[] authentication;
}
