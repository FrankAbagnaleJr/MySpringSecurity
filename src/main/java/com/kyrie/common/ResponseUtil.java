package com.kyrie.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: jijin
 * @date: 2023/8/5 19:44 周六
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
public class ResponseUtil {
    public static void out(HttpServletResponse response, ResultModel resultModel) {

        ObjectMapper objectMapper = new ObjectMapper();

        //封装response的状态码
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //封装response的内容，也就是返回类转json格式
        try {
            //使用jackson，把json格式的resultModel写入到response输出流中
            objectMapper.writeValue(response.getOutputStream(),resultModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
