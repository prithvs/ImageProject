package com.learn.ImageProject;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageProjectApplication {
		
	//Swagger Documentation
	//http://localhost:8080/swagger-ui/index.html#/
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(ImageProjectApplication.class, args);
	}

}
