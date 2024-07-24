package com.apimobilestore;

import org.springframework.boot.SpringApplication;

public class TestApiMobileStoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(ApiMobileStoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
