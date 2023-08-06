package com.kyrie.service.impl;

import com.kyrie.entity.LoginUser;
import com.kyrie.entity.User;
import com.kyrie.mapper.LoginUserMapper;
import com.kyrie.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @auther: jijin
 * @date: 2023/8/6 18:53 周日
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LoginUserMapper loginUserMapper;

    @Override
    public User selectByUsername(String username) {
        LoginUser loginUser = loginUserMapper.selectByUsername(username);
        if (!Objects.isNull(loginUser)) {
            User user = new User();
            BeanUtils.copyProperties(loginUser, user);
            return user;
        }
        return null;
    }
}
