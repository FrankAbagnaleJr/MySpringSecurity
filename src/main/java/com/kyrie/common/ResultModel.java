package com.kyrie.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: jijin
 * @date: 2023/8/4 23:29 周五
 * @project_name: QFSecuretyPorject
 * @version: 1.0
 * @description TODO
 */
@Data
public class ResultModel<T> implements Serializable {

    //状态码
    private int code;

    //信息
    private String message;

    //数据
    private T data;

    private static ResultModel resultModel = new ResultModel();

    public static ResultModel success() {
        resultModel.setCode(200);
        resultModel.setMessage("success");
        resultModel.setData(null);
        return resultModel;
    }
    public static ResultModel success(String message) {
        resultModel.setCode(200);
        resultModel.setMessage(message);
        resultModel.setData(null);
        return resultModel;
    }
    public static ResultModel success(Object data) {
        resultModel.setCode(200);
        resultModel.setMessage("success");
        resultModel.setData(data);
        return resultModel;
    }
    public static ResultModel success(String message,Object data) {
        resultModel.setCode(200);
        resultModel.setMessage(message);
        resultModel.setData(data);
        return resultModel;
    }
    public static ResultModel error() {
        resultModel.setCode(500);
        resultModel.setMessage("error");
        resultModel.setData(null);
        return resultModel;
    }
    public static ResultModel error(int code,String message) {
        resultModel.setCode(code);
        resultModel.setMessage(message);
        resultModel.setData(null);
        return resultModel;
    }

}
