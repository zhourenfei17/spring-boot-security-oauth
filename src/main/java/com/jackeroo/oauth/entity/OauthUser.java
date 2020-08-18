package com.jackeroo.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * oauth统一认证用户
 * @author alex
 * @date 2020/08/11
 */
@Data
@TableName("oauth_user")
public class OauthUser implements Serializable {
    private static final long serialVersionUID = -3693279689434442688L;
    @TableId
    private Long id;
    /**
     * 账号
     */
    private String account;
    /**
     * 昵称
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机
     */
    private String phone;
    /**
     * 性别，1：男，2：女
     */
    private Integer gender;
    /**
     * 状态，0：正常，1：冻结
     */
    private Integer status;
    /**
     * 删除标识，0：正常，1：已删除
     */
    private Integer delFlag;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    /**
     * 用户拥有的角色列表
     */
    @TableField(exist = false)
    private List<OauthRole> roleList;
}
