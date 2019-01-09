package com.wyd.bootstrap.security.constant;

public enum LoginType {
    UNAUTHORIZED("00000000","未登录。"),
    AUTHORIZED("10000000","登录成功。"),
    AUTHORIZED_FAILED("00000002","授权失败。");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    LoginType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
