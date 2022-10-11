package com.example.buddyByte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BuddyByteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuddyByteApplication.class, args);
	}

}
