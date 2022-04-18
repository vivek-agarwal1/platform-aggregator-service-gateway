package com.oyorooms.gateway.external.requestGenerator;

import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.Request.ValidateTokenRequest;
import com.oyorooms.gateway.helper.Constants;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IdentityRequestGeneratorTest {

    IdentityRequestGenerator identityRequestGenerator;

    ValidateTokenRequest expectedValidateTokenRequest;

    @BeforeAll
    void setup() {
        identityRequestGenerator = new IdentityRequestGenerator();
        int length = 100;
        boolean useLetters = true;
        boolean useNumbers = false;
        String expectedAuthToken = RandomStringUtils.random(length, useLetters, useNumbers);
        List<String> expectedAdditionalFields = Constants.ADDITIONAL_FIELDS;
        List<String> expectedUserModes = Arrays.asList("Consumer_Guest", "CorporateGuest");
        expectedValidateTokenRequest = ValidateTokenRequest.builder()
                .additionalKeys(expectedAdditionalFields)
                .authToken(expectedAuthToken)
                .userModes(expectedUserModes)
                .namespace(Constants.CRS)
                .build();
    }

    private void assertAllValidateTokenRequest(ValidateTokenRequest expectedValidateTokenRequest,
                                               ValidateTokenRequest validateTokenRequest){
        assertAll(
                () -> expectedValidateTokenRequest.getAuthToken()
                        .equals(validateTokenRequest.getAuthToken()),
                () -> expectedValidateTokenRequest.getAdditionalKeys()
                        .equals(validateTokenRequest.getAdditionalKeys()),
                () -> expectedValidateTokenRequest.getUserModes()
                        .equals(validateTokenRequest.getUserModes()),
                () -> expectedValidateTokenRequest.getNamespace()
                        .equals(validateTokenRequest.getNamespace()),
                () -> expectedValidateTokenRequest.equals(validateTokenRequest),
                () -> expectedValidateTokenRequest.toString()
                        .equals(validateTokenRequest.toString()),
                () -> Objects.nonNull(validateTokenRequest.hashCode())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"access-token", "access_token"})
    void getValidateTokenRequestWithAccessToken(String accessToken) throws UnauthorizedException {
        MockServerHttpRequest request = MockServerHttpRequest.post("/")
                .header(accessToken, expectedValidateTokenRequest.getAuthToken())
                .build();
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        ValidateTokenRequest validateTokenRequest = identityRequestGenerator
                .getValidateTokenRequest(mockServerWebExchange);

        assertAllValidateTokenRequest(expectedValidateTokenRequest, validateTokenRequest);
    }

    @Test
    void getValidateTokenRequestWithoutAccessToken() throws UnauthorizedException {
        MockServerHttpRequest request = MockServerHttpRequest.post("/")
                .build();
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        assertThrowsExactly(UnauthorizedException.class, () -> identityRequestGenerator
                .getValidateTokenRequest(mockServerWebExchange));
    }

    @ParameterizedTest
    @ValueSource(strings = {"access-token", "access_token"})
    void getValidateTokenRequestWithValidatingAllKeysExceptAccessToken(String accessToken)
            throws UnauthorizedException {
        MockServerHttpRequest request = MockServerHttpRequest.post("/")
                .header(accessToken, expectedValidateTokenRequest.getAuthToken())
                .queryParam(Constants.USER_MODES, expectedValidateTokenRequest.getUserModes().get(0))
                .queryParam(Constants.USER_MODES, expectedValidateTokenRequest.getUserModes().get(1))
                .build();

        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        ValidateTokenRequest validateTokenRequest = identityRequestGenerator
                .getValidateTokenRequest(mockServerWebExchange);

        assertAllValidateTokenRequest(expectedValidateTokenRequest, validateTokenRequest);
    }
}