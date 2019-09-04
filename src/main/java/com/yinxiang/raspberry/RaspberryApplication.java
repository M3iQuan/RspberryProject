package com.yinxiang.raspberry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yinxiang.raspberry.mapper")

public class RaspberryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaspberryApplication.class, args);
	}

}
