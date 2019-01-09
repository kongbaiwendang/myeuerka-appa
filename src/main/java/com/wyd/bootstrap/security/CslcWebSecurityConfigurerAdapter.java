package com.wyd.bootstrap.security;

import com.wyd.bootstrap.security.jwt.JwtAuthenticationEntryPoint;
import com.wyd.bootstrap.security.manager.CslcAuthenticationManager;
import com.wyd.bootstrap.security.service.CslcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 用于配置系统访问权限路径
 * @author wangyadong
 *
 */
@EnableWebSecurity
public class CslcWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{

	@Autowired
	private CslcAuthenticationManager cslcAuthenticationManager;

	@Resource
	private CslcUserDetailsService cslcUserDetailsService;

	/**
	 * 登录失败后处理的类
	 */
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
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
				.antMatchers("/login/*","/swagger/**").permitAll()//所有用户都可以登录
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//				.formLogin().loginProcessingUrl("/login/getAuthorizationToken").and()

				//关掉表单登录验证
//	        .formLogin().successHandler(new JwtAuthenticationSuccessHandler())
//	            .and()
				.csrf().disable();
//				.addFilter(new JWTLoginFilter(authenticationManager()));
//	        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//		http.authorizeRequests()
//			.antMatchers("/*").permitAll();//设置路径匿名访问权限
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//设置认证管理器
		auth.authenticationProvider(this.cslcAuthenticationManager);
		auth.userDetailsService(this.cslcUserDetailsService);
	}


}
