package com.kyrie.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther: jijin
 * @date: 2023/8/5 20:04 周六
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Component
public class TokenManager {

    //设置token的密钥(盐)
    @Value("{token.securityKey}")
    private String securityKey;

    //过期时间
    @Value("{token.securityExp}")
    private long day;
    //过期时间毫秒值
    private long exp = 60 * 60 * 24 * day;

    /**
     * 使用jwt生成token
     * @param username
     * @return
     */
    public String createToken(String username) {
        String token = Jwts.builder()
                //
                .setSubject(username)
                //什么时候过期
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                //私钥的算法，私钥(签名、盐)
                .signWith(SignatureAlgorithm.HS256, securityKey)
                //得到token
                .compact();
        return token;
    }

    /**
     * 用jwt解析token获得用户名
     * @param token
     * @return
     */
    public String getUsernameForParseToken(String token) {
        Claims claims = Jwts.parser()
                //传入密钥
                .setSigningKey(securityKey)
                //传入token
                .parseClaimsJws(token)
                //得到claims对象
                .getBody();
        String subject = claims.getSubject();
        return subject;
    }

}
