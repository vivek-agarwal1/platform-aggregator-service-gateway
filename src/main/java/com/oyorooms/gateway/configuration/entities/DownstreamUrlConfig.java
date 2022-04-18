package com.oyorooms.gateway.configuration.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "downstream.api")
@Configuration
public class DownstreamUrlConfig {

    private String omsReadUrl;

    private String omsWriteUrl;

    private String crsAllUrl;

    private String fulfilmentCreateUrl;
}
