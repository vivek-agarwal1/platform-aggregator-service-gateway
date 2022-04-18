package com.oyorooms.gateway.external.service;

import com.oyorooms.gateway.configuration.entities.IdentityConfig;
import com.oyorooms.gateway.core.util.WebClientUtil;
import com.oyorooms.gateway.exception.ExternalServiceException;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.ExternalServiceExecutor;
import com.oyorooms.gateway.external.Request.ValidateTokenRequest;
import com.oyorooms.gateway.external.constants.IdentityConstants;
import com.oyorooms.gateway.external.response.BaseExternalResponse;
import com.oyorooms.gateway.external.requestGenerator.IdentityRequestGenerator;
import com.oyorooms.gateway.helper.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ValidateTokenExecutor implements ExternalServiceExecutor {

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private IdentityConfig identityConfig;

    @Autowired
    IdentityRequestGenerator identityRequestGenerator;

    @Override
    public <T extends BaseExternalResponse> Mono<T> execute(ServerWebExchange exchange, Class<T> responseClass) throws UnauthorizedException {
        ValidateTokenRequest validateTokenRequest = identityRequestGenerator.getValidateTokenRequest(exchange);
        WebClient webClient = getIdentityWebClient();
        String uri = IdentityConstants.VALIDATE_TOKEN_ENDPOINT + validateTokenRequest.convertToQueryString();
        return webClient.get().uri(uri).retrieve()
                .onStatus(HttpStatus::isError,
                        response -> Mono.error(new ExternalServiceException(
                                String.valueOf(response.statusCode().value()))))
                .bodyToMono(responseClass);
    }

    private WebClient getIdentityWebClient() {
        WebClient.Builder webClientBuilder = webClientUtil.getHttpWebClientBuilder();
        WebClient webClient = webClientBuilder.baseUrl(identityConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, IdentityConstants.CONTENT_TYPE)
                .defaultHeader(Constants.ACCESS_TOKEN, identityConfig.getAccessToken())
                .build();
        return webClient;
    }
}
