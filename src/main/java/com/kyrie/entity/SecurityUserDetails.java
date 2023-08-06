package com.kyrie.entity;

import com.alibaba.druid.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @auther: jijin
 * @date: 2023/8/6 13:53 周日
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */

public class SecurityUserDetails implements UserDetails {

    //当前登录的用户
    private User currentUser;

    //当权登录用户的权限
    private List<String> permissionList;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionLisg(List<String> permissionLisg) {
        this.permissionList = permissionLisg;
    }

    // TODO 获取权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (String permission : permissionList) {
//            if (!StringUtils.isEmpty(permission)) {
//                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
//                authorities.add(simpleGrantedAuthority);
//            }
//        }

        permissionList.stream().forEach(a -> {
            if (StringUtils.isEmpty(a)) {
                authorities.add(new SimpleGrantedAuthority(a));
            }
        });

        return authorities;
    }

    // TODO 获取密码
    @Override
    public String getPassword() {
        return this.currentUser.getPassword();
    }

    // TODO 获取用户名
    @Override
    public String getUsername() {
        return this.currentUser.getUsername();
    }

    // TODO 账号是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO 账号是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO 凭证是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO 是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
