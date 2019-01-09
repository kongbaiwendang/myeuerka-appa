package com.wyd.bootstrap.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wyd.bootstrap.security.filter.JwtAuthenticationSuccessHandler;
import com.wyd.bootstrap.security.service.CslcUserDetailsService;
import com.wyd.bootstrap.security.service.impl.CslcUserDetailsServiceImpl;
/**
 * Spring Security 登录验证类，可进行自定义扩展
 * @author wangyadong
 *
 */
@EnableWebSecurity
public class WebSecurityConfig  implements WebMvcConfigurer {
	/**
	 * 登录验证方法，此处需要修改为查询数据库的方式，引入数据查询
	 * @return
	 * @throws Exception
	 */
	@Bean
    public CslcUserDetailsService userDetailsService() throws Exception {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
		CslcUserDetailsService manager = new CslcUserDetailsServiceImpl();
        return manager;
    }
	
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		String idForEncode = "bcrypt";
		Map encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());

		PasswordEncoder passwordEncoder =
		    new DelegatingPasswordEncoder(idForEncode, encoders);
		return passwordEncoder;
		
	}
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}
	

}
