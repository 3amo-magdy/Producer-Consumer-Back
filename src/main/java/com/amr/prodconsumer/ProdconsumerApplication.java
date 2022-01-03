package com.amr.prodconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ProdconsumerApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ProdconsumerApplication.class, args);

	}

}
