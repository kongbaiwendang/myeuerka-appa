package com.wyd.bootstrap.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyd.bootstrap.controller.ApiResponse;
import com.wyd.bootstrap.security.constant.LoginType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@EnableWebSecurity
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(LoginType.UNAUTHORIZED.name());
        apiResponse.setResponseCode(LoginType.UNAUTHORIZED.getCode());
        apiResponse.setData(LoginType.UNAUTHORIZED.getDesc());
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
