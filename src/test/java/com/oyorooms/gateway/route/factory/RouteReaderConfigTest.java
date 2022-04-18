package com.oyorooms.gateway.route.factory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RouteReaderConfigTest {

    @Spy
    RouteReaderConfig routeReaderConfig;

    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void readData() throws IOException {
        assertNull(routeReaderConfig.readData());
    }
}