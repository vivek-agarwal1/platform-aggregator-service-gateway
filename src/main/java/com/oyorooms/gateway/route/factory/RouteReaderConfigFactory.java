package com.oyorooms.gateway.route.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oyorooms.gateway.configuration.entities.RouteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RouteReaderConfigFactory {

    @Autowired
    private RouteConfig routeReaderConfig;

    public RouteReaderConfig getRouteClass(String channel)
    {
        if (Objects.isNull(channel) || channel.isEmpty()) {
            return null;
        } else if ("YAML".equals(channel)) {
            ObjectMapper objectmapper = new ObjectMapper(new YAMLFactory());
            return new YMLRouteReaderConfig(routeReaderConfig.getFilePath(), objectmapper);
        }
        return null;
    }
}
