package com.wordanalysis.hanlpservice;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@EnableSwagger2Doc
@SpringBootApplication
@PropertySource("classpath:hanlp.properties")
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
public class HanlpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanlpServiceApplication.class, args);
	}
}
