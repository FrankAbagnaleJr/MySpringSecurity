package com.kyrie.config;

import com.kyrie.filter.TokenAuthFilter;
import com.kyrie.filter.TokenLoginFilter;
import com.kyrie.security.TokenLogoutHandler;
import com.kyrie.security.TokenManager;
import com.kyrie.security.UnAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @auther: jijin
 * @date: 2023/8/4 23:13 周五
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Configuration
@EnableWebSecurity  //开启Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true)  //方法级别的验证，发起请求之前做认证，默认是false
public class TokenSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RedisTemplate redisTemplate;

    //账号密码查询数据库的校验逻辑，需要重写这个接口的实现类
    @Autowired
    private UserDetailsService userDetailsService;

    //做一个web请求的时候一个配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                //没有权限时的处理方案
                .authenticationEntryPoint(new UnAuthEntryPoint())
                //关闭csrf
                .and().csrf().disable()
                //要求所有接口都需要做权限认证
                .authorizeRequests()
                .anyRequest().authenticated()
                //注销的路径
                .and().logout().logoutUrl("/user/logout")
                //注销的处理器:准备一个专门操作token的类 + redisTemplate
                .addLogoutHandler(new TokenLogoutHandler(tokenManager, redisTemplate))
                //TODO 添加自定义的拦截器
                .and()
                //添加一个登录的拦截器需要做的逻辑,
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                //添加一个处理权限的拦截器
                .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, redisTemplate))
                //开启http的请求头的方式认证
                .httpBasic();
    }

    /**
     * 密码加密
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获得用户名和密码的方式,是通过userDetailsService实现类对象获得的，userDetailsService是自定义校验账号密码的逻辑
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(this.passwordEncoder());
    }
}
