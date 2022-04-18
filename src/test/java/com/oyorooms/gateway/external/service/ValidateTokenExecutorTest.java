package com.oyorooms.gateway.external.service;

import com.oyorooms.gateway.configuration.entities.IdentityConfig;
import com.oyorooms.gateway.core.util.WebClientUtil;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.Request.ValidateTokenRequest;
import com.oyorooms.gateway.external.requestGenerator.IdentityRequestGenerator;
import com.oyorooms.gateway.external.response.IdentityResponse;
import com.oyorooms.gateway.helper.Constants;
import com.oyorooms.gateway.helper.MockConstants;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidateTokenExecutorTest {

    @InjectMocks
    private ValidateTokenExecutor validateTokenExecutor;

    @Mock
    private WebClientUtil webClientUtil;

    @Mock
    private IdentityConfig identityConfig;

    @Mock
    private IdentityRequestGenerator identityRequestGenerator;


    @BeforeAll
    void setup() {
        identityConfig = new IdentityConfig();
        identityConfig.setAccessToken(MockConstants.IDENTITY_ACCESS_TOKEN);
        identityConfig.setUrl(MockConstants.IDENTITY_MOCK_URL);
        identityConfig = Mockito.mock(identityConfig.getClass());
        identityRequestGenerator = new IdentityRequestGenerator();
        identityRequestGenerator = Mockito.mock(identityRequestGenerator.getClass());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testExecute() throws UnauthorizedException {
        MockServerHttpRequest request = MockServerHttpRequest.post("/").build();
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();
        ValidateTokenRequest validateTokenRequest = ValidateTokenRequest.builder().build();

        Mockito.doReturn(WebClient.builder()).when(webClientUtil).getHttpWebClientBuilder();
        Mockito.doReturn(validateTokenRequest).when(identityRequestGenerator)
                .getValidateTokenRequest(mockServerWebExchange);

        assertNotNull(validateTokenExecutor.execute(mockServerWebExchange, IdentityResponse.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"access-token", "access_token"})
    void testExecuteForRequestWithAdditionalKeysAndQueryParams(String accessToken) throws UnauthorizedException {
        int length = 100;
        boolean useLetters = true;
        boolean useNumbers = false;
        List<String> expectedUserModeList = Arrays.asList("ConsumerGuest", "BusinessGuest");
        String expectedAuthToken = RandomStringUtils.random(length, useLetters, useNumbers);
        MockServerHttpRequest request = MockServerHttpRequest.post("/")
                .header(accessToken, expectedAuthToken)
                .queryParam(Constants.USER_MODES, expectedUserModeList.get(0))
                .queryParam(Constants.USER_MODES, expectedUserModeList.get(1))
                .build();
        ValidateTokenRequest validateTokenRequest = ValidateTokenRequest.builder().build();
        validateTokenRequest.setAdditionalKeys(Arrays.asList("user_mode"));
        validateTokenRequest.setUserModes(Arrays.asList("ConsumerGuest", "BusinessGuest"));
        validateTokenRequest.setAuthToken(MockConstants.ACCESS_TOKEN);
        validateTokenRequest.setNamespace(Constants.CRS);
        MockServerWebExchange mockServerWebExchange = MockServerWebExchange.builder(request).build();

        Mockito.doReturn(WebClient.builder()).when(webClientUtil).getHttpWebClientBuilder();
        Mockito.doReturn(validateTokenRequest).when(identityRequestGenerator)
                .getValidateTokenRequest(Mockito.any(ServerWebExchange.class));

        assertNotNull(validateTokenExecutor.execute(mockServerWebExchange, IdentityResponse.class));
    }
}