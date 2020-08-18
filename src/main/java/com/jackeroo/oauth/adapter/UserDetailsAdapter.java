package com.jackeroo.oauth.adapter;

import com.jackeroo.oauth.entity.OauthRole;
import com.jackeroo.oauth.entity.OauthUser;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 使用了对象适配器模式
 * OauthUser数据库实体对象 到 Security Oauth2提供的UserDetails的对象适配器
 * @author alex
 * @date 2020/07/23
 */
@Setter
public class UserDetailsAdapter implements UserDetails {

    // fastJson反序列化的时候需要有属性去接受redis中的属性值
    private String username;
    private String password;
    /**
     * 权限
     */
    private List<GrantedAuthority> authorities;
    private boolean enable;

    /**
     * 角色的默认前缀
     * @see {@link org.springframework.security.access.expression.SecurityExpressionRoot#setDefaultRolePrefix}
     */
    private static final String defaultRolePrefix = "ROLE_";

    // 由于使用了FastJson进行序列化和反序列化，因此必须要有一个空的构造器
    public UserDetailsAdapter(){

    }

    public UserDetailsAdapter(OauthUser oauthUser) {
        this.username = oauthUser.getAccount();
        this.password = oauthUser.getPassword();

        List<GrantedAuthority> list = new ArrayList<>();
        if(!StringUtils.isEmpty(oauthUser.getRoleList())){
            for (OauthRole role : oauthUser.getRoleList()) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(defaultRolePrefix + role.getRoleCode());
                list.add(grantedAuthority);
            }
        }
        this.authorities = list;
        this.enable = (oauthUser.getDelFlag() == 0 && oauthUser.getStatus() == 0);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
