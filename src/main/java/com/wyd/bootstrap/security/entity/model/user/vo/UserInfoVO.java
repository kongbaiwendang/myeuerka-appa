package com.wyd.bootstrap.security.entity.model.user.vo;

import java.util.Date;

public class UserInfoVO {
    private String id;

    private String userAuxiliaryId;

    private String username;

    private String name;

    private String password;

    private Date validDateStart;

    private Date validDateEnd;

    private String isValid;

    private String openId;

    private String commercialId;

    private String sessionKey;

    /**
     * 注册标志 0未注册 1已注册
     */
    private String registFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getValidDateStart() {
        return validDateStart;
    }

    public void setValidDateStart(Date validDateStart) {
        this.validDateStart = validDateStart;
    }

    public Date getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(Date validDateEnd) {
        this.validDateEnd = validDateEnd;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(String commercialId) {
        this.commercialId = commercialId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getRegistFlag() {
        return registFlag;
    }

    public void setRegistFlag(String registFlag) {
        this.registFlag = registFlag;
    }

    public String getUserAuxiliaryId() {
        return userAuxiliaryId;
    }

    public void setUserAuxiliaryId(String userAuxiliaryId) {
        this.userAuxiliaryId = userAuxiliaryId;
    }
}
