package com.wyd.bootstrap.security.manager;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import com.wyd.bootstrap.security.service.CslcUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;


@EnableWebSecurity
public class CslcAuthenticationManager implements AuthenticationProvider {
	private Logger logger = LoggerFactory.getLogger(CslcAuthenticationManager.class);

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected boolean hideUserNotFoundExceptions = true;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CslcUserDetailsService cslcUserDetailsService;

	/**
	 * 自定义校验方法
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
				() -> messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only UsernamePasswordAuthenticationToken is supported"));

		// Determine username
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

		UserInfo user = null;
		try {
			user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
		} catch (UsernameNotFoundException notFound) {
			logger.debug("User '" + username + "' not found");

			if (hideUserNotFoundExceptions) {
				throw new BadCredentialsException(messages
						.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
			} else {
				throw notFound;
			}
		}

		try {
			additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
		} catch (AuthenticationException exception) {
			throw exception;
		}

		Object principalToReturn = user;
		return createSuccessAuthentication(principalToReturn, authentication, user);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	/**
	 * 获取用户数据
	 * 
	 * @param username
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	protected final UserInfo retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		try {
			UserInfo loadedUser = cslcUserDetailsService.getSysUserByUsername(username);
			if (loadedUser == null) {
				logger.error("用户:{} 不存在。",username);
				throw new InternalAuthenticationServiceException(
						"CslcUserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		} catch (Exception ex) {
			logger.error("用户:{} 登录失败。",username,ex);
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	/**
	 * 密码校验
	 * 
	 * @param userInfo
	 * @param authentication
	 * @throws AuthenticationException
	 */
	protected void additionalAuthenticationChecks(UserInfo userInfo, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(presentedPassword, userInfo.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	/**
	 * 登录成功赋权token
	 * 
	 * @param principal
	 * @param authentication
	 * @param user
	 * @return
	 */
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
														 UserInfo user) {
		// Ensure we return the original credentials the user supplied,
		// so subsequent attempts are successful even with encoded passwords.
		// Also ensure we return the original getDetails(), so that future
		// authentication events after cache expiry contain the details
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
				authentication.getCredentials(), authoritiesMapper.mapAuthorities(null));
		result.setDetails(authentication.getDetails());

		return result;
	}
}
