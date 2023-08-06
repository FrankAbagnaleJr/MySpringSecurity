package com.kyrie.service;

import com.kyrie.entity.User;

public interface UserService {
    User selectByUsername(String username);
}
