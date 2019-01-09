package com.wyd.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.wyd.bootstrap.**.*Mapper")
public class MyeurekaAppaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyeurekaAppaApplication.class, args);
	}
}
