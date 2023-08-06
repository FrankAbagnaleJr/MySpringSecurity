package com.kyrie.security;

import com.alibaba.druid.util.StringUtils;
import com.kyrie.common.ResponseUtil;
import com.kyrie.common.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther: jijin
 * @date: 2023/8/5 20:47 周六
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */

public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 注销用户时候，执行的方法，1.删除用户 2.redis删除权限
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //1.从请求头中获得前端携带的token
        String token = request.getHeader("token");

        //判断如果携带token就解析
        //2.使用jwt解析token
        if (!StringUtils.isEmpty(token)) {
            //得到用户名
            String username = tokenManager.getUsernameForParseToken(token);
            //3.删除redis中的用户的权限
            redisTemplate.delete(username);
        }

        ResponseUtil.out(response, ResultModel.success("注销成功"));
    }
}
