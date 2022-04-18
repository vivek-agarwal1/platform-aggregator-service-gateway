package com.oyorooms.gateway;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableAutoConfiguration(exclude = {WebMvcAutoConfiguration.class })
@SpringBootApplication
@EnableWebFlux
public class IdentityGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityGatewayServiceApplication.class, args);
		PrometheusMeterRegistry prometheusRegistry =
				new PrometheusMeterRegistry( PrometheusConfig.DEFAULT );
	}

}
