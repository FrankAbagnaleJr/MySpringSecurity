package com.kyrie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jijinliang
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_login_user")
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String username;

    private String password;

    private String salt;

    private String wxUnionid;

    private String nickname;

    private String name;

    private String userpic;

    private String companyId;

    private String utype;

    private LocalDateTime birthday;

    private String sex;

    private String email;

    private String cellphone;

    private String qq;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
