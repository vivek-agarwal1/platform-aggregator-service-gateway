package com.oyorooms.gateway.configuration.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "config.yml.reader")
@Configuration
public class RouteConfig extends BaseConfig {

    private String filePath;
}
