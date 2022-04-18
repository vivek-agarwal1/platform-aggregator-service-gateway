package com.oyorooms.gateway.route.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oyorooms.gateway.core.entity.RouteEntity;
import com.oyorooms.gateway.core.entity.RouteDataEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class YMLRouteReaderConfig extends RouteReaderConfig {

    private String pathFile;

    private ObjectMapper objectMapper;

    @Override
    public List<RouteEntity> readData() throws IOException, IllegalArgumentException {
        InputStream inputStream = getFileFromResourceAsStream(pathFile);
        RouteDataEntity routeDataPoints = objectMapper.readValue(inputStream, RouteDataEntity.class);
        return routeDataPoints.getRouteData();
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
