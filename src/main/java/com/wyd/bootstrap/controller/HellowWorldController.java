package com.wyd.bootstrap.controller;

import com.wyd.bootstrap.entity.dto.demo.DemoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(value="/hellow")
public class HellowWorldController {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@ResponseBody
	@RequestMapping(value="/getUserInfo")
	public ApiResponse<DemoVO> getUserInfo(){
		DemoVO demoVO = new DemoVO();
		demoVO.setName("jack");
		demoVO.setAge(20);
		demoVO.setSex("male");
		ApiResponse<DemoVO> response = new ApiResponse<>();
		response.setStatus("success");
		response.setData(demoVO);
		return response;
	}
	
	/**
	 * 向缓存放入数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/putUserInfoByRedis")
	public ApiResponse<DemoVO> putUserInfoByRedis(){
		DemoVO demoVO = new DemoVO();
		demoVO.setName("john");
		demoVO.setAge(25);
		demoVO.setSex("male");
		redisTemplate.opsForValue().set("john", demoVO);
		ApiResponse<DemoVO> response = new ApiResponse<>();
		response.setStatus("success");
		response.setData(demoVO);
		return response;
	}
	
	/**
	 * 从缓存取出数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getUserInfoByRedis")
	public ApiResponse<DemoVO> getUserInfoByRedis(){
		DemoVO demoVO = (DemoVO)redisTemplate.opsForValue().get("john");
		ApiResponse<DemoVO> response = new ApiResponse<>();
		response.setStatus("success");
		response.setData(demoVO);
		return response;
	}
	
}
