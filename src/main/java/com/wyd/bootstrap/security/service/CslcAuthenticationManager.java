package com.wyd.bootstrap.security.service;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;


@EnableWebSecurity
public class CslcAuthenticationManager implements AuthenticationProvider {
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected boolean hideUserNotFoundExceptions = true;
	private boolean forcePrincipalAsString = false;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	private volatile String userNotFoundEncodedPassword;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CslcUserDetailsService cslcUserDetailsService;

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

		if (forcePrincipalAsString) {
			principalToReturn = user.getUsername();
		}
//		return authentication;
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
		prepareTimingAttackProtection();
		try {
			UserInfo loadedUser = cslcUserDetailsService.getSysUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"CslcUserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		} catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		} catch (InternalAuthenticationServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	/**
	 * 判断是否启用了密码策略
	 */
	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		}
	}

	/**
	 * 查询用户异常处理
	 * 
	 * @param authentication
	 */
	private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
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
		System.out.println(presentedPassword);
		System.out.println(passwordEncoder.encode(presentedPassword));
		System.out.println(passwordEncoder.matches(presentedPassword, userInfo.getPassword()));
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
