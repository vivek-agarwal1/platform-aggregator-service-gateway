package com.oyorooms.gateway.core.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Spy;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomWebFluxTagsProviderTest {

    @Spy
    CustomWebFluxTagsProvider customWebFluxTagsProvider;

    @BeforeAll
    void setup() {
        customWebFluxTagsProvider = new CustomWebFluxTagsProvider();
    }

    @Test
    void httpRequestTagsWithNoPath() {
        MockServerHttpRequest request = MockServerHttpRequest.post("")
                .build();
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        AtomicBoolean testPass = new AtomicBoolean(false);
        customWebFluxTagsProvider.httpRequestTags(mockServerWebExchange, null).forEach(tag -> {
            if(tag.getKey().equals("uri") && tag.getValue().equals("root")){
                testPass.set(true);
            }
        });
        assertTrue(testPass.get());
    }

    @Test
    void httpRequestTagsWithPath() {
        MockServerHttpRequest request = MockServerHttpRequest.post("/insurance/bill")
                .build();
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        AtomicBoolean testPass = new AtomicBoolean(false);
        customWebFluxTagsProvider.httpRequestTags(mockServerWebExchange, null).forEach(tag -> {
            if(tag.getKey().equals("uri") && tag.getValue().equals("/insurance/bill")){
                testPass.set(true);
            }
        });
        assertTrue(testPass.get());
    }
}