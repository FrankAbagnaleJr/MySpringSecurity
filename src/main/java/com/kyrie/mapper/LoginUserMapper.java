package com.kyrie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyrie.entity.LoginUser;
import com.kyrie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jijinliang
 * @since 2023-08-01
 */
@Mapper
public interface LoginUserMapper extends BaseMapper<User> {
    LoginUser selectByUsername(String username);
}
