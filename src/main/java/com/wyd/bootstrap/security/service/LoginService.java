package com.wyd.bootstrap.security.service;

import com.wyd.bootstrap.controller.ApiResponse;
import com.wyd.bootstrap.security.entity.model.user.UserInfo;

import java.util.Map;

/**
 * 抽象登录服务接口定义
 */
public interface LoginService {

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    public UserInfo getUserInfo(UserInfo user);

    /**
     * 生成JwsToken
     * @param userInfo
     * @return
     */
    public String generateJwsToken(UserInfo userInfo);

    /**
     * 检查登录信息是否正确
     * @param data
     */
    public ApiResponse loginCheck(Map<String,String> data);
}
