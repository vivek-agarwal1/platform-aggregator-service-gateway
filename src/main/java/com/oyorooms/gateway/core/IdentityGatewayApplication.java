package com.oyorooms.gateway.core;

import com.oyorooms.gateway.route.RouteLocatorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class IdentityGatewayApplication {

    @Autowired
    private RouteLocatorImplementation routeLocatorImplementation;

    @Bean
    public RouteLocator routeLocator() throws IOException {
        return routeLocatorImplementation.appRoutes();
    }
}
