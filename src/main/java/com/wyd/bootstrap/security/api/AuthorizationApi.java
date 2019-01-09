package com.wyd.bootstrap.security.api;

import com.wyd.bootstrap.controller.ApiResponse;
import com.wyd.bootstrap.security.constant.LoginType;
import com.wyd.bootstrap.security.constant.ResultType;
import com.wyd.bootstrap.security.entity.model.user.vo.UserInfoVO;
import com.wyd.bootstrap.security.service.impl.WeChatLoginService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

@EnableWebSecurity
@RestController
@RequestMapping(path = "/login")
public class AuthorizationApi {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationApi.class);

    @Autowired
    private AuthenticationProvider authenticationManager;

    @Autowired
    private WeChatLoginService weChatLoginService;

    /**
     * 微信登录认证方法
     * @param request
     * @param response
     * @param data
     * @return
     */
    @RequestMapping(path = "/getWeChatAuthorizationToken")
    public ApiResponse getWeChatAuthorizationToken(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,String> data){
        //合规校验
        ApiResponse apiResponse = weChatLoginService.loginCheck(data);
        if(StringUtils.isBlank(apiResponse.getResponseCode())){
            return apiResponse;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setCommercialId(data.get("commercialId"));//主办方Id

        Map rspMap = null;
        try {
            rspMap = weChatLoginService.getOpenIdWithCode((String) data.get("code"));
        } catch (Exception e) {
            logger.error("微信接口调用失败。",e);
            apiResponse.setData("与微信交互失败。");
            apiResponse.setStatus(ResultType.FAIL.name());
            apiResponse.setResponseCode(ResultType.FAIL.getCode());
            return apiResponse;
        }


        String openId = null;
        String sessionKey = null;
//        openId = "31346546156316546";
        //下面的调试时需可注释
        if(rspMap != null && rspMap.get("openid") != null){
            //openId和session_key,session_key的有效期不定，在解密用户敏感信息时使用，因此，如果解密失败，应要求前端重新登录授权
            openId = (String) rspMap.get("openid");
            sessionKey = (String) rspMap.get("openId和session_key");
        }else{
            apiResponse.setResponseCode(LoginType.AUTHORIZED_FAILED.getCode());
            apiResponse.setStatus(LoginType.AUTHORIZED_FAILED.name());
            apiResponse.setData(LoginType.AUTHORIZED_FAILED.getDesc());
            return apiResponse;
        }
        //判断用户是否登录过，新用户需要保存。

        userInfoVO.setOpenId(openId);
        userInfoVO.setSessionKey(sessionKey);


        //微信用户将openId和session_key放入认证信息中，交由权限模块Security处理
        UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(openId,sessionKey,new ArrayList<>());
//        //调用鉴权管理器
        authenticationManager.authenticate(authen);


        //TODO 解析JSON,JWT token生成
        //暂时JWT中只放OpenId

        //微信访问只能获取到openId和session_key
        return apiResponse;
    }
}
