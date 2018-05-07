package com.nchu.easyword.exception;

/**
 * Service层统一异常
 */
public class ServiceException extends Exception {
    private StatusCode statusCode;
    private String msg;

    public ServiceException() {
        super();
    }

    public ServiceException(StatusCode statusCode, String msg) {
        super(msg);
        this.msg = msg;
        this.statusCode = statusCode;
    }

    /*如果只给定状态码则默认将状态码设置为异常信息*/
    public ServiceException(StatusCode statusCode) {
        super(statusCode.getDesc());
        this.statusCode = statusCode;
        this.msg = statusCode.getDesc();
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    /*设置自定义异常信息*/
    public void setMessage(String message) {
        this.msg = message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
