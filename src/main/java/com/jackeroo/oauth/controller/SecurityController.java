package com.jackeroo.oauth.controller;

import com.jackeroo.oauth.common.utils.MD5Util;
import com.jackeroo.oauth.common.utils.RandImageUtil;
import com.jackeroo.oauth.common.utils.RandomUtils;
import com.jackeroo.oauth.common.vo.Result;
import com.jackeroo.oauth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author alex
 * @date 2020/07/17
 */
@Controller
public class SecurityController {
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("oauthTest")
    public String oauthTest(){
        return "oauthTest";
    }

    @RequestMapping("resourceTest")
    public String resourceTest(){
        return "resourceTest";
    }

    @GetMapping("/code/{key}")
    @ResponseBody
    public Result getCode(@PathVariable String key){
        String code = RandomUtils.randomGen(4);
        String lowerCaseCode = code.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");

        redisTemplate.opsForValue().set(realKey, code, 5, TimeUnit.MINUTES);

        try {
            String base64 = RandImageUtil.generate(code);
            return Result.ok(base64);
        } catch (IOException e) {
            return Result.error("获取验证码错误");
        }
    }

}
