package com.engineercms.hanlpservice;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableSwagger2Doc
@SpringBootApplication
@PropertySource("classpath:hanlp.properties")
public class HanlpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanlpServiceApplication.class, args);
	}
}
