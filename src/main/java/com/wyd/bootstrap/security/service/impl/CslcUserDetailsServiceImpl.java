package com.wyd.bootstrap.security.service.impl;

import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import com.wyd.bootstrap.security.mapper.UserInfoMapper;
import com.wyd.bootstrap.security.service.CslcUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

public class CslcUserDetailsServiceImpl implements CslcUserDetailsService{

	@Resource
	private UserInfoMapper userInfoMapper;

	@Override
	@Deprecated
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getSysUserByUsername(String username) {
		// TODO Auto-generated method stub
		//临时做个假数据
		UserInfo userInfo = null;
		if("user1".equals(username)) {
			userInfo = new UserInfo();
			userInfo.setId("00001");
			userInfo.setRealName("用户1");
			userInfo.setPassword("{bcrypt}$2a$10$CQTr0OWzo6F8DH6uMg.qHuTaSBLLz/KzKbnx9orCn83CniUyiwZwK");
			userInfo.setUsername("user1");
		}
		userInfo = userInfoMapper.selectByPrimaryKey(username);
		return userInfo;
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserInfo createSysUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getUserInfoByOpenId() {
		return null;
	}

}
