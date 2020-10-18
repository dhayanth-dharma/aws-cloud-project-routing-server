package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = ContextRegionProviderAutoConfiguration.class)

@EnableSwagger2
public class CcApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcApplication.class, args);
	}

}
