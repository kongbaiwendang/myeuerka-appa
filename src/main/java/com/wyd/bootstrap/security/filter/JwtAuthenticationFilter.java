package com.wyd.bootstrap.security.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;
import com.wyd.bootstrap.entity.constant.security.JwtDecodeResultType;
import com.wyd.bootstrap.security.util.JwtUtil;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
					throws IOException, ServletException {
		final boolean debug = this.logger.isDebugEnabled();
		String authorization = request.getHeader("Authorization");

		try {
			//头信息为空，则直接跳过，让security自己检测转向登录页面
			if (authorization == null || !authorization.startsWith("Bearer ")) {
				SecurityContextHolder.clearContext();
				chain.doFilter(request, response);
				throw new BadCredentialsException(SpringSecurityMessageSource.getAccessor().getMessage("AbstractLdapAuthenticationProvider.emptyPassword"));
			}
			//解析jwt，获得jwt中的信息,并校验是否超时
			Map<String, Object> jwtResult = null;
			try {
				jwtResult = JwtUtil.valid(authorization.replace("Bearer ", ""));
			} catch (ParseException | JOSEException e) {
				logger.error("JwtUtil.valid failed,authorization: "+authorization, e);
				throw new BadCredentialsException(SpringSecurityMessageSource.getAccessor().getMessage("AbstractLdapAuthenticationProvider.emptyPassword"));
			}
			int type = (int) jwtResult.get("Result");
			if(type == JwtDecodeResultType.SUCCESS.getType()) {
				//解析成功，获取存储对象
				JSONObject jwtData = (JSONObject) jwtResult.get("data");
				JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
				UserInfo userInfo = null;
				try {
					userInfo = jsonParser.parse(((JSONObject)jwtData.get("data")).toJSONString(),UserInfo.class);
				} catch (net.minidev.json.parser.ParseException e) {
					logger.error("Parse sysuser from jwt failed,jwtData: "+jwtData, e);
					throw new BadCredentialsException(SpringSecurityMessageSource.getAccessor().getMessage("AbstractLdapAuthenticationProvider.emptyPassword"));
				}
				if(userInfo != null) {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
							userInfo, userInfo.getPassword(),null);
					SecurityContextHolder.getContext().setAuthentication(authRequest);
					
				}
			}else if(type == JwtDecodeResultType.TIMEOUT.getType()) {
				
			}else {
				
			}
		}
		catch (AuthenticationException failed) {
			SecurityContextHolder.clearContext();

			if (debug) {
				this.logger.debug("Authentication request for failed: " + failed);
			}
			return;
		}
		chain.doFilter(request, response);
	}
	
}
