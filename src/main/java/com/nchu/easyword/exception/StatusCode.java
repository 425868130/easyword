package com.nchu.easyword.exception;

/**
 * @author xujw
 * 2018-4-18 17:43:16
 * 自定义响应状态返回码
 */
public enum StatusCode {
    SUCCESS(1000, "请求成功"),
    UN_LOGIN(2000, "用户未登录"),
    PERMISSION_DENIED(3000, "权限不足拒绝访问"),
    REQUEST_FAILED(4000, "请求失败"),
    REPEAT(5000, "重复操作"),
    TIMEOUT(6000, "请求超时"),
    SYSTEM_ERROR(7000, "系统异常");
    private int code;
    private String desc;

    StatusCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
