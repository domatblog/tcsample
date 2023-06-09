package com.asseco.blog.tcsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TcsampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcsampleApplication.class, args);
	}

}
