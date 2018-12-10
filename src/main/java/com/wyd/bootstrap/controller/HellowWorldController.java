package com.wyd.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wyd.bootstrap.controller.vo.ResponseVO;
import com.wyd.bootstrap.entity.dto.demo.DemoVO;
import com.wyd.bootstrap.entity.vo.demo.DemoDTO;
@RestController
@RequestMapping(value="/hellow")
public class HellowWorldController {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@ResponseBody
	@RequestMapping(value="/getUserInfo")
	public ResponseVO<DemoVO> getUserInfo(){
		DemoVO demoVO = new DemoVO();
		demoVO.setName("jack");
		demoVO.setAge(20);
		demoVO.setSex("male");
		ResponseVO<DemoVO> response = new ResponseVO<>();
		response.setStatus("success");
		response.setValue(demoVO);
		return response;
	}
	
	/**
	 * 向缓存放入数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/putUserInfoByRedis")
	public ResponseVO<DemoVO> putUserInfoByRedis(){
		DemoVO demoVO = new DemoVO();
		demoVO.setName("john");
		demoVO.setAge(25);
		demoVO.setSex("male");
		redisTemplate.opsForValue().set("john", demoVO);
		ResponseVO<DemoVO> response = new ResponseVO<>();
		response.setStatus("success");
		response.setValue(demoVO);
		return response;
	}
	
	/**
	 * 从缓存取出数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getUserInfoByRedis")
	public ResponseVO<DemoVO> getUserInfoByRedis(){
		DemoVO demoVO = (DemoVO)redisTemplate.opsForValue().get("john");
		ResponseVO<DemoVO> response = new ResponseVO<>();
		response.setStatus("success");
		response.setValue(demoVO);
		return response;
	}
	
}
