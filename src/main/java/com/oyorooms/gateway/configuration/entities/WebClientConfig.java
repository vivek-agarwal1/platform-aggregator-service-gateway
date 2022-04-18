package com.oyorooms.gateway.configuration.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "http.web-client-util")
@Configuration
public class WebClientConfig extends BaseConfig{

    private Integer readTimeout;

    private Integer writeTimeout;

    private Integer connectionTimeout;

    private Integer connectTimeout;

}
