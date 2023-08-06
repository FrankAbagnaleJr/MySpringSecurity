package com.kyrie.filter;

import com.alibaba.druid.util.StringUtils;
import com.kyrie.security.TokenManager;
import io.jsonwebtoken.lang.Collections;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @auther: jijin
 * @date: 2023/8/6 15:38 周日
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;


    public TokenAuthFilter(AuthenticationManager authenticationManager,
                           TokenManager tokenManager,
                           RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * TODO 权限相关的操作
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获得token(从请求头中获取)
        String token = request.getHeader("token");

        if (!StringUtils.isEmpty(token)) {
            //得到username
            String username = tokenManager.getUsernameForParseToken(token);
            //根据用户名从redis中查找用户对应的权限
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            //将取出的权限存入到权限上下文中，表示当前token对应的用户具备哪些权限
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (!Collections.isEmpty(permissionValueList)) {
                for (String permissionlist : permissionValueList) {
                    //转换成描述权限的对象
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionlist);
                    authorities.add(simpleGrantedAuthority);
                }
            }
            //生成权限信息对象，就是用来封装权限的
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, token, authorities);
            //把权限存入权限上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //放行
        chain.doFilter(request,response);

    }
}
