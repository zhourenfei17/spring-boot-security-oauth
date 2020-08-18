package com.jackeroo.oauth;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.jackeroo.oauth.common.utils.RandomUtils;
import com.jackeroo.oauth.entity.OauthClientDetails;
import com.jackeroo.oauth.entity.OauthRole;
import com.jackeroo.oauth.entity.OauthUser;
import com.jackeroo.oauth.entity.OauthUserRole;
import com.jackeroo.oauth.service.OauthClientDetailsService;
import com.jackeroo.oauth.service.OauthRoleService;
import com.jackeroo.oauth.service.OauthUserDetailService;
import com.jackeroo.oauth.service.OauthUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityOauthApplicationTests {

    @Resource
    private OauthUserDetailService userDetailService;
    @Resource
    private OauthRoleService roleService;
    @Resource
    private OauthUserRoleService userRoleService;
    @Resource
    private OauthClientDetailsService oauthClientDetailsService;

    /**
     * 添加用户和角色
     */
    @Test
    public void createUserAndRole(){
        OauthRole role = new OauthRole();
        role.setRoleCode("admin");
        role.setRoleName("超级管理员");
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());

        roleService.save(role);
        log.info("创建角色成功，角色id【{}】", role.getId());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        OauthUser user = new OauthUser();
        user.setAccount("admin");
        user.setUserName("超级管理员");
        user.setPassword(bCryptPasswordEncoder.encode("123456"));
        user.setPhone("13866666666");
        user.setGender(1);
        user.setStatus(0);
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userDetailService.save(user);

        log.info("创建用户成功，用户id【{}】", user.getId());

        OauthUserRole userRole = new OauthUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
    }

    /**
     * 创建第三方接入应用信息
     */
    @Test
    public void createClientDetails(){
        OauthClientDetails details = new OauthClientDetails();
        details.setAppName("微信");
        details.setAppKey(String.valueOf(new Sequence().nextId()));
        String appSecret = RandomUtils.randomGen(32);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        details.setAppSecret(bCryptPasswordEncoder.encode(appSecret));
        details.setResourceIds("USER-RESOURCE");
        details.setScope("read,write");
        details.setAuthorizedGrantTypes("authorization_code,password,refresh_token");
        details.setRedirectUri("http://localhost:8090/index");
        details.setAuthorities("admin");
        details.setTrusted(0);
        details.setCreateTime(LocalDateTime.now());
        details.setUpdateTime(LocalDateTime.now());

        oauthClientDetailsService.save(details);

        log.info("保存第三方应用信息成功，appKey【{}】，appSecret【{}】，请妥善保存", details.getAppKey(), appSecret);
    }
}
