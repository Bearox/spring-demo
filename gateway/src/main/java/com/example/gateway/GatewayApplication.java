package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author Bearox
 */
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				// Add a simple re-route from: /get to: http://httpbin.org:80
				// Add a simple "Hello:World" HTTP Header
				.route(p -> p
						.path("/uic/api/hello") // intercept calls to the /get path
						.filters(f -> f.addRequestHeader("previous", "gateway")) // add header
						.uri("http://localhost:8081/uic/api/hello")) // forward to httpbin
				.build();
	}

}
