package com.jackeroo.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jackeroo.oauth.dao")
public class SpringBootSecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityOauthApplication.class, args);
    }

}
