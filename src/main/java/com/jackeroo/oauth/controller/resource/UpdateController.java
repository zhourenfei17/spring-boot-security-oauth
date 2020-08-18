package com.jackeroo.oauth.controller.resource;

import com.jackeroo.oauth.common.vo.Result;
import com.jackeroo.oauth.service.OauthUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 * @date 2020/08/13
 */
@RestController
@RequestMapping("/update")
public class UpdateController {
    @Autowired
    private OauthUserDetailService userDetailService;

    /**
     * 修改用户姓名
     * @param username
     * @return
     */
    @RequestMapping("username")
    public Result updateUserName(String username){
        if(username == null || "".equals(username.trim())){
            return Result.error("用户姓名不能为空");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails)authentication.getPrincipal();

        if(userDetailService.updateUserName(principal.getUsername(), username)){
            return Result.ok();
        }else{
            return Result.error("修改用户姓名失败");
        }
    }
}
