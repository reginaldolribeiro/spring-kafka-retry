package com.springkafka.springkafkaretry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@EnableRetry
public class SpringKafkaRetryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaRetryApplication.class, args);
	}

	@GetMapping
	public String hello(){
		return "Hello world!!!";
	}

}
