package com.jackeroo.oauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackeroo.oauth.entity.OauthUser;

/**
 * @author alex
 * @date 2020/08/11
 */
public interface OauthUserMapper extends BaseMapper<OauthUser> {

    OauthUser getUserByAccount(String account);
}
