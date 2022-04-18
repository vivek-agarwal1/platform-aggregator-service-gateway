package com.oyorooms.gateway.route.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oyorooms.gateway.configuration.entities.RouteConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteReaderConfigFactoryTest {

    @InjectMocks
    private RouteReaderConfigFactory routeReaderConfigFactory;

    @Spy
    private RouteConfig routeReaderConfig;

    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRouteClassForNullChannel() {
        assertNull(routeReaderConfigFactory.getRouteClass(null));
    }

    @Test
    public void getRouteClassForBlankChannel() {
        assertNull(routeReaderConfigFactory.getRouteClass(""));
    }

    @Test
    public void getRouteClassForYAMLChannel() {
        YMLRouteReaderConfig ymlRouteReaderConfig = new YMLRouteReaderConfig(routeReaderConfig.getFilePath(), new ObjectMapper(new YAMLFactory()));
        assertAll(
                () -> ymlRouteReaderConfig.getClass().equals(routeReaderConfigFactory.getRouteClass("YAML").getClass()),
                () -> Objects.nonNull(routeReaderConfigFactory.getRouteClass("YAML"))
        );
    }

    @Test
    public void getRouteClassForNonExistentChannel() {
        assertNull(routeReaderConfigFactory.getRouteClass("Non-Existent"));
    }
}