package com.nchu.easyword.dto;

import com.nchu.easyword.exception.StatusCode;

/**
 * 2018-4-3 09:29:11
 *
 * @author xujw
 * 统一返回前端的数据库格式
 */
public class ResponseDTO<T> {
    /*状态码*/
    private int statusCode;
    /*用户信息是否被更新*/
    private boolean isUserUpdate;
    /*附加消息*/
    private String message;
    /*响应数据*/
    private T data;

    public ResponseDTO() {
    }

    public ResponseDTO(StatusCode statusCode, boolean isUserUpdate, String message) {
        this.statusCode = statusCode.getcode();
        this.isUserUpdate = isUserUpdate;
        this.message = message;
    }

    public ResponseDTO(StatusCode statusCode, boolean isUserUpdate, String message, T data) {
        this.statusCode = statusCode.getcode();
        this.isUserUpdate = isUserUpdate;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ResponseDTO setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode.getcode();
        /*如果消息为空则默认使用状态码消息*/
        if (this.message == null || this.message.isEmpty()) {
            message = statusCode.getDesc();
        }
        return this;
    }

    public boolean isUserUpdate() {
        return isUserUpdate;
    }

    public ResponseDTO setUserUpdate(boolean userUpdate) {
        isUserUpdate = userUpdate;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseDTO setData(T data) {
        this.data = data;
        return this;
    }

    public ResponseDTO setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
