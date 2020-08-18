package com.jackeroo.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * oauth统一认证角色
 * @author alex
 * @date 2020/08/11
 */
@Data
@TableName("oauth_role")
public class OauthRole implements Serializable {
    private static final long serialVersionUID = 1275468202514560808L;
    @TableId
    private Long id;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;

    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
}
