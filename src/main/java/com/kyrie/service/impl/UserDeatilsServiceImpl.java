package com.kyrie.service.impl;

import com.kyrie.entity.User;
import com.kyrie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @auther: jijin
 * @date: 2023/8/6 14:29 周日
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Service("userDetailsService")
public class UserDeatilsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名从数据库中查找用户
        User user = userService.selectByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("当前用户不存在");
        }


        return null;
    }
}
