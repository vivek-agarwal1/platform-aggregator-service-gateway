package com.oyorooms.gateway.route.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oyorooms.gateway.core.entity.RouteDataEntity;
import com.oyorooms.gateway.core.entity.RouteEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class YMLRouteReaderConfigTest {

    private static YMLRouteReaderConfig ymlRouteReaderConfig;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup(){
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper = Mockito.mock(objectMapper.getClass());
        ymlRouteReaderConfig = new YMLRouteReaderConfig("",objectMapper);
    }

    @Test
    public void readDataWithNonExistentResourceFile() {
        String filePath = "routes-nonExistent.yml";
        YMLRouteReaderConfig ymlRouteReaderConfig = new YMLRouteReaderConfig(filePath, objectMapper);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            ymlRouteReaderConfig.readData();
        });
        String expectedMessage = "file not found! " + filePath;
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @ParameterizedTest
    @ValueSource(strings = {"development", "staging", "production"})
    public void testReadDataWithExistentResourceFilesThrowsIOException(String development) throws IOException {
        String expectedMessage = "Mocking IO Exception";
        ymlRouteReaderConfig = new YMLRouteReaderConfig("routes-" +
                development + ".yml", objectMapper);
        Mockito.doThrow(new IOException(expectedMessage)).when(objectMapper)
                .readValue(Mockito.any(InputStream.class), Mockito.eq(RouteDataEntity.class));
        Exception exception = assertThrows(IOException.class, () ->{
            ymlRouteReaderConfig.readData();
        });
        assertTrue(expectedMessage.equals(exception.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"development", "staging", "production"})
    public void testReadDataWithExistentResourceFilesThrowsNotNullPointerException(String development)
            throws IOException {
        ymlRouteReaderConfig = new YMLRouteReaderConfig("routes-" +
                development + ".yml", new ObjectMapper(new YAMLFactory()));

        assertDoesNotThrow(() -> ymlRouteReaderConfig.readData());
    }

    @ParameterizedTest
    @ValueSource(strings = {"development", "staging", "production"})
    public void testReadDataWithMockedResourceFileForAllEnvironment(String environment) throws IOException {
        String expectedMessage = "Mocking IO Exception";
        List<String> routeFiltersMock = Arrays.asList("routesfilter1", "routesfilter2");
        RouteDataEntity routeDataEntity = new RouteDataEntity();
        List<RouteEntity> routeEntityListMock = new ArrayList<>();
        Arrays.asList(1, 2).forEach(i -> {
            RouteEntity routeEntity = new RouteEntity();
            routeEntity.setPath("testpath" + i);
            routeEntity.setMethod("testMethod" + i);
            routeEntity.setUri("https://testhost" + i + ".com");
            routeEntity.setName("testName" + i);
            routeEntity.setFilters(routeFiltersMock);
            routeEntityListMock.add(routeEntity);
        });
        routeDataEntity.setRouteData(routeEntityListMock);
        ymlRouteReaderConfig = new YMLRouteReaderConfig("routes-" +
                environment + ".yml", objectMapper);
        Mockito.doReturn(routeDataEntity).when(objectMapper)
                .readValue(Mockito.any(InputStream.class), Mockito.eq(RouteDataEntity.class));
        assertArrayEquals(new List[]{routeEntityListMock}, new List[]{ymlRouteReaderConfig.readData()});
    }

    @Test
    public void testNoArgsConstructorOfYmlRouteReaderConfig() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Integer expectedValue = 0;
        YMLRouteReaderConfig ymlRouteReaderConfigNoArgsConstructor = new YMLRouteReaderConfig();
        Constructor<YMLRouteReaderConfig> constructor = (Constructor<YMLRouteReaderConfig>) ymlRouteReaderConfigNoArgsConstructor
                .getClass().getDeclaredConstructor();
        assertTrue(expectedValue.equals(constructor.getParameterCount()));
    }
}