package com.wyd.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.wyd.bootstrap.security.mapper")
public class MyeurekaAppaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyeurekaAppaApplication.class, args);
	}
}
