package com.oyorooms.gateway.filter;

import com.oyorooms.gateway.filter.customFilter.AuthenticationFilterAndApplySignature;
import com.oyorooms.gateway.filter.customFilter.RemoveAccessTokenFilter;
import com.oyorooms.gateway.filter.customFilter.UnauthorizedRequestFilter;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomGatewayFilterFactoryTest {

    @InjectMocks
    private CustomGatewayFilterFactory customGatewayFilterFactory;

    @Spy
    private AuthenticationFilterAndApplySignature authenticationFilter;

    @Spy
    private UnauthorizedRequestFilter unauthorizedRequestFilter;

    @Spy
    private RemoveAccessTokenFilter removeAccessTokenFilter;

    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCustomGatewayFilterWithFilterNameAuthenticationFilter() {
        AbstractGatewayFilterFactory abstractGatewayFilterFactory =
                customGatewayFilterFactory.getCustomGatewayFilter("AuthenticationFilter");
        assertAll(
                () -> Objects.nonNull(abstractGatewayFilterFactory),
                () -> abstractGatewayFilterFactory.getClass().equals(
                        authenticationFilter.getClass())
        );
    }

    @Test
    void getCustomGatewayFilterWithFilterNameUnauthorizedCheckFilter() {
        AbstractGatewayFilterFactory abstractGatewayFilterFactory =
                customGatewayFilterFactory.getCustomGatewayFilter("UnauthorizedCheckFilter");
        assertAll(
                () -> Objects.nonNull(abstractGatewayFilterFactory),
                () -> abstractGatewayFilterFactory.getClass().equals(
                        unauthorizedRequestFilter.getClass())
        );
    }

    @Test
    void getCustomGatewayFilterWithFilterNameRemoveAccessTokenFilter() {
        AbstractGatewayFilterFactory abstractGatewayFilterFactory =
                customGatewayFilterFactory.getCustomGatewayFilter("RemoveAccessTokenFilter");
        assertAll(
                () -> Objects.nonNull(abstractGatewayFilterFactory),
                () -> abstractGatewayFilterFactory.getClass().equals(
                        removeAccessTokenFilter.getClass())
        );
    }

    @Test
    void getCustomGatewayFilterDefaultReturn() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        assertNull(customGatewayFilterFactory.getCustomGatewayFilter(RandomStringUtils
                .random(length, useLetters, useNumbers)));
    }
}