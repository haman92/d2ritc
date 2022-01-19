package com.blizzard.d2ritemauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class JavaWebMVCSampleOAuthClient {

	public static void main(String[] args) {
		SpringApplication.run(JavaWebMVCSampleOAuthClient.class, args);
	}

}
