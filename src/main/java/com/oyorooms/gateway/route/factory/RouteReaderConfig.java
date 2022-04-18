package com.oyorooms.gateway.route.factory;

import com.oyorooms.gateway.core.entity.RouteEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class RouteReaderConfig {

    public List<RouteEntity> readData() throws IOException, IllegalArgumentException {
        return null;
    }
}
