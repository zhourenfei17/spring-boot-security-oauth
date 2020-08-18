package com.jackeroo.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author alex
 * @date 2020/08/11
 */
@Data
@TableName("oauth_user_role")
public class OauthUserRole implements Serializable {
    private static final long serialVersionUID = 2489654110281415444L;

    @TableId
    private Long id;
    private Long userId;
    private Long roleId;
}
