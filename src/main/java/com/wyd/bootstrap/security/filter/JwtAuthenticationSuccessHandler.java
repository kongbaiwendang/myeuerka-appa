package com.wyd.bootstrap.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.nimbusds.jose.JOSEException;
import com.wyd.bootstrap.security.util.JwtUtil;
/**
 * 用于向认证成功的客户端下发token
 * @author wangyadong
 *
 */
public class JwtAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		UserInfo sysUser = (UserInfo) authentication.getPrincipal();
		Map<String, Object> payloadMap = JwtUtil.generateJwsPayload();
		payloadMap.put("data", sysUser);
		String token = null;
		try {
			token = JwtUtil.generateJwsToken(payloadMap);
		} catch (JOSEException e) {
			logger.error("generate jwt token error!",e);
		}
		response.addHeader("Authorization", "Bearer "+ token);
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
