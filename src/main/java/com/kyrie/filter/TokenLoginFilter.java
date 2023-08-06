package com.kyrie.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyrie.common.ResponseUtil;
import com.kyrie.common.ResultModel;
import com.kyrie.entity.User;
import com.kyrie.security.TokenManager;
import com.kyrie.entity.SecurityUserDetails;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @auther: jijin
 * @date: 2023/8/5 21:19 周六
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
    }

    /**
     * TODO 得到用户名和密码的过程，如果认证成功，调用下面重写的成功方法，失败则调用下面重写的失败方法
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //获取表单提供的数据
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            //得到user对象，就要校验账号密码,也就是认证的过程
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword(), new ArrayList<>()
                    )
            );
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO 认证成功执行的方法,做两件事，1.生成token给前端，2.把用户的权限存入redis中
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        //得到用户
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) authResult.getPrincipal();
        //从用户对象得到用户名
        String username = securityUserDetails.getUsername();
        //生成token
        String token = tokenManager.createToken(username);
        //存入到redis中， username:权限
        List<String> permissionList = securityUserDetails.getPermissionList(); //得到权限
        redisTemplate.opsForValue().set(username, permissionList, 10, TimeUnit.SECONDS);
        //返回token给前端,调用out方法把返回类对象写入response中
        ResponseUtil.out(response, ResultModel.success(token));
    }

    /**
     * TODO 认证失败执行的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        //认证失败把ResultModel对象转成json封装到response中返回
        ResultModel resultModel = ResultModel.error(401, failed.getMessage());
        ResponseUtil.out(response,resultModel);
    }
}
