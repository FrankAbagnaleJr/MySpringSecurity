package com.kyrie.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: jijin
 * @date: 2023/8/6 12:33 周日
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String token;
}
