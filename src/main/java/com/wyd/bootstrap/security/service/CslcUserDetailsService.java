package com.wyd.bootstrap.security.service;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface CslcUserDetailsService extends UserDetailsService{
	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return SysUser 用户基本信息
	 */
	public UserInfo getSysUserByUsername(String username);
	
	/**
	 * 修改用户密码
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePassword(String oldPassword, String newPassword);
	
	/**
	 * 判断用户是否存在
	 * @param username
	 * @return 用户存在结果(真假)
	 */
	public boolean userExists(String username);
	
	/**
	 * 
	 * @return
	 */
	public UserInfo createSysUser();
}
