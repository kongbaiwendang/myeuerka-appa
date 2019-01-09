package com.wyd.bootstrap.security.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyd.bootstrap.controller.ApiResponse;
import com.wyd.bootstrap.security.constant.RegistType;
import com.wyd.bootstrap.security.constant.ResultType;
import com.wyd.bootstrap.security.entity.model.user.UserAuxiliary;
import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import com.wyd.bootstrap.security.entity.model.user.vo.UserInfoVO;
import com.wyd.bootstrap.security.mapper.UserAuxiliaryMapper;
import com.wyd.bootstrap.security.mapper.UserInfoMapper;
import com.wyd.bootstrap.security.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WeChatLoginService extends AbstractLoginService {

    private static Logger logger = LoggerFactory.getLogger(WeChatLoginService.class);

    /**
     * 微信地址
     */
    private static String wxurl;

    /**
     * 小程序 appId
     */
    private static String appid;

    /**
     * 小程序 appSecret
     */
    private static String secret;

    @Value("${wechat.openapiurl}")
    public void setWxurl(String wxurl) {
        WeChatLoginService.wxurl = wxurl;
    }

    @Value("${wechat.appid}")
    public void setAppid(String appid) {
        WeChatLoginService.appid = appid;
    }

    @Value("${wechat.secret_key}")
    public void setSecret(String secret) {
        WeChatLoginService.secret = secret;
    }

    /**
     * 固定写死
     */
    private static String grant_type="authorization_code";

    @Autowired
    private UserAuxiliaryMapper userAuxiliaryMapper;

    public Map getOpenIdWithCode(String code) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("appid",appid);
        map.put("secret",secret);
        map.put("grant_type",grant_type);
        map.put("js_code",code);
        String response = null;
        try {
            response = HttpUtil.sendGetRequest(wxurl,map);
        } catch (Exception e) {
            logger.error("调用微信获取OpenId失败。",e);
            throw new Exception(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map resultMap = objectMapper.readValue(response,Map.class);
        return resultMap;
    }

    public ApiResponse loginCheck(Map<String,String> data) {
        ApiResponse apiResponse = new ApiResponse();
        if(data != null && data.get("code") != null && data.get("commercialId") != null){
            apiResponse.setData("入参不合法。");
            apiResponse.setStatus(ResultType.FAIL.name());
            apiResponse.setResponseCode(ResultType.FAIL.getCode());
        }
        return apiResponse;
    }

    /**
     * 根据openId处理用户数据
     * @param userInfoVO
     */
    @Transactional
    public void saveUserInfoByOpenId(UserInfoVO userInfoVO){

        //获取数据库中userinfo表数据
        UserInfo dbUser = getUserInfoByOpenId(userInfoVO.getOpenId());
        UserAuxiliary userAuxiliary = userAuxiliaryMapper.selectByOpenId(userInfoVO.getOpenId());
        if(dbUser != null && StringUtils.isNotBlank(dbUser.getId())){
            userInfoVO.setId(dbUser.getId());//用于认证回写数据
            userInfoVO.setRegistFlag(RegistType.REGISTED.getCode());
        }else{
            userInfoVO.setRegistFlag(RegistType.UNREGISTED.getCode());
        }
        if(userAuxiliary != null && StringUtils.isNotBlank(userAuxiliary.getId())){
            userInfoVO.setUserAuxiliaryId(userAuxiliary.getId());
            userAuxiliary.setSessionKey(userInfoVO.getSessionKey());
            userAuxiliary.setOpenId(userInfoVO.getOpenId());
            userAuxiliary.setCommercialId(userInfoVO.getCommercialId());
        }else{
            userAuxiliary = new UserAuxiliary();
            userAuxiliary.setId(UUID.randomUUID().toString());
            userAuxiliary.setSessionKey(userInfoVO.getSessionKey());
            userAuxiliary.setOpenId(userInfoVO.getOpenId());
            userAuxiliary.setCommercialId(userInfoVO.getCommercialId());
            userAuxiliary.setUserInfoId(userInfoVO.getId());
            userInfoVO.setUserAuxiliaryId(userAuxiliary.getId());
        }
        //微信登录只更新扩展表数据即可，报名时绑定userinfo信息，并进行权限更新
        userAuxiliaryMapper.insertOrUpdateUserAuxiliary(userAuxiliary);
    }

    /**
     * 根据OpenId获取用户数据
     * @param openId
     * @return
     */
    public UserInfo getUserInfoByOpenId(String openId){
        UserInfo userInfo = null;
        if(StringUtils.isNotBlank(openId)){
            userInfo = userAuxiliaryMapper.getUserInfoByOpenId(openId);
        }
        return userInfo;
    }

    @Override
    public UserInfo getUserInfo(UserInfo userInfo) {
        return null;
    }

}
