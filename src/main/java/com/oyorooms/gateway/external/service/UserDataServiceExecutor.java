package com.oyorooms.gateway.external.service;

import com.oyorooms.gateway.configuration.entities.IdentityConfig;
import com.oyorooms.gateway.core.util.WebClientUtil;
import com.oyorooms.gateway.exception.ExternalServiceException;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.ExternalServiceExecutor;
import com.oyorooms.gateway.external.constants.IdentityConstants;
import com.oyorooms.gateway.external.response.BaseExternalResponse;
import com.oyorooms.gateway.helper.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class UserDataServiceExecutor implements ExternalServiceExecutor {

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private IdentityConfig identityConfig;

    @Override
    public <T extends BaseExternalResponse> Mono<T> execute(ServerWebExchange exchange, Class<T> responseClass)
            throws ExternalServiceException, UnauthorizedException {
        WebClient webClient = getIdentityWebClient();
        ServerHttpRequest request = exchange.getRequest();
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/users/get_users")
                        // .queryParams(createQueryParams(userSearchRequest))
                        .queryParam("phone", "123")
                        .build())
                .retrieve()
//                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
//                        Mono.error(new UpstreamServerUnavailableException("userProfiles host not available",
//                                clientResponse.rawStatusCode()))
//                )
                .bodyToMono(Object.class)
             //   .publishOn(Schedulers.fromExecutor(executorConfig.getUserByParamsExecutor()))
             //   .timeout(Duration.ofMillis(timeoutMsGetUsersByParams))
//                .onErrorResume(throwable -> {
//                    throw new UpstreamServerUnavailableException(502);
//                })
                .block();
        return null;
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
