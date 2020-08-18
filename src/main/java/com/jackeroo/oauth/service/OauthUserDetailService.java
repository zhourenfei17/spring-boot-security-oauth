package com.jackeroo.oauth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackeroo.oauth.adapter.UserDetailsAdapter;
import com.jackeroo.oauth.dao.OauthUserMapper;
import com.jackeroo.oauth.entity.OauthRole;
import com.jackeroo.oauth.entity.OauthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author alex
 * @date 2020/07/17
 */
@Service
public class OauthUserDetailService extends ServiceImpl<OauthUserMapper, OauthUser> implements UserDetailsService, Serializable {

    private static final long serialVersionUID = 1170885289644276974L;
    @Resource
    private OauthUserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OauthUser user = mapper.getUserByAccount(username);
        if(user == null){
            throw new UsernameNotFoundException("该用户不存在");
        }

        return new UserDetailsAdapter(user);
    }

    /**
     * 获取用户受保护的信息
     * @param account
     * @return
     */
    public JSONObject getProtectedUserInfo(String account){
        OauthUser user = mapper.getUserByAccount(account);
        if(user != null){
            JSONObject result = new JSONObject();
            result.put("id", user.getId());
            result.put("userName", user.getUserName());
            result.put("phone", user.getPhone());
            result.put("gender", user.getGender());
            if(!CollectionUtils.isEmpty(user.getRoleList())){
                result.put("role", user.getRoleList().stream().map(OauthRole::getRoleCode).collect(Collectors.toList()));
            }

            return result;
        }
        return null;
    }

    /**
     * 修改用户姓名
     * @param account
     * @param userName
     * @return
     */
    public boolean updateUserName(String account, String userName){
        OauthUser user = mapper.getUserByAccount(account);
        if(user != null){
            user.setUserName(userName);
            user.setUpdateTime(LocalDateTime.now());
            mapper.updateById(user);

            return true;
        }
        return false;
    }
}
