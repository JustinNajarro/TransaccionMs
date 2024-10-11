package com.nttdata.TransaccionMs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TransaccionMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransaccionMsApplication.class, args);
	}

}
