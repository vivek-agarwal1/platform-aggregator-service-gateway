package com.oyorooms.gateway.configuration.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "external.api.identity")
@Configuration
public class IdentityConfig extends BaseConfig{

    private String url;

    private String accessToken;
}
