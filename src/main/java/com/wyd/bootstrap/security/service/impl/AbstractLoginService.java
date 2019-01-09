package com.wyd.bootstrap.security.service.impl;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import com.wyd.bootstrap.security.service.LoginService;

/**
 * 登录模块抽象类，实现公共的登录赋权方法
 */
public abstract class AbstractLoginService implements LoginService {

    @Override
    public abstract UserInfo getUserInfo(UserInfo user);

    /**
     * 生成JwsToken共享方法
     * @param userInfo
     * @return
     */
    @Override
    public String generateJwsToken(UserInfo userInfo) {
        return null;
    }


}
