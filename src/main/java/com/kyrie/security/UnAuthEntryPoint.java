package com.kyrie.security;

import com.kyrie.common.ResponseUtil;
import com.kyrie.common.ResultModel;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: jijin
 * @date: 2023/8/4 23:25 周五
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
public class UnAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * 权限认证失败会调用的方法，会往response里面封装一下东西，最后response会返回给前端
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //没有权限的时候返回给前端一个统一的格式：状态码、信息、数据
        ResponseUtil.out(response,ResultModel.error());
    }
}
