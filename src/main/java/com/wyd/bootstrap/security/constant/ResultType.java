package com.wyd.bootstrap.security.constant;

public enum ResultType {
    SUCCESS("10000000","请求成功。"),
    FAIL("100000001","请求失败。");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ResultType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
