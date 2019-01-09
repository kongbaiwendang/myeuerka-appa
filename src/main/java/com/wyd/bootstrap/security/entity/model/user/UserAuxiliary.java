package com.wyd.bootstrap.security.entity.model.user;

public class UserAuxiliary {
    private String id;

    private String userInfoId;

    private String openId;

    private String sessionKey;

    private String commercialId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId == null ? null : userInfoId.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey == null ? null : sessionKey.trim();
    }

    public String getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(String commercialId) {
        this.commercialId = commercialId;
    }
}