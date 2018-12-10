package com.wyd.bootstrap.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wyd.bootstrap.security.filter.JwtAuthenticationFilter;
import com.wyd.bootstrap.security.filter.JwtAuthenticationSuccessHandler;

/**
 * 用于配置系统访问权限路径
 * @author wangyadong
 *
 */
@EnableWebSecurity
public class CslcWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
	/**
	 * 详细的HttpSecurity的配置，用于过滤允许的路径
	 * 此处应查数据库配置
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	            .anyRequest().authenticated()
	            .and()
	        .formLogin().successHandler(new JwtAuthenticationSuccessHandler())
	            .and()
	        .httpBasic()
	        	.and()
	        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//		http.authorizeRequests()
//			.antMatchers("/*").permitAll();//设置路径匿名访问权限
	}
	
	
}
