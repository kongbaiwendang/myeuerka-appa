package com.wyd.bootstrap.security.constant;

public enum RegistType {
    UNREGISTED("0","未注册"),
    REGISTED("1","已注册");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    RegistType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
