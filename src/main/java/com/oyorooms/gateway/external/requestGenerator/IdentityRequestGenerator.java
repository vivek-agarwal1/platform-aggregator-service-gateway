package com.oyorooms.gateway.external.requestGenerator;

import com.oyorooms.gateway.core.enums.StatusCode;
import com.oyorooms.gateway.core.util.LoggerUtil;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.Request.ValidateTokenRequest;
import com.oyorooms.gateway.helper.Constants;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class IdentityRequestGenerator {

    public ValidateTokenRequest getValidateTokenRequest(ServerWebExchange exchange) throws UnauthorizedException {
        Boolean userModesPresent = isUserModesPresent(exchange);
        List<String> additionalFields = Constants.ADDITIONAL_FIELDS;
        String token = getUriEncodedToken(exchange);
        if(Objects.isNull(token)){
            throw new UnauthorizedException(10, StatusCode.UNAUTHORIZED.getMsg(), StatusCode.UNAUTHORIZED.getType());
        }
        ValidateTokenRequest validateTokenRequest = ValidateTokenRequest.builder().authToken(token).namespace(Constants.CRS).build();
        if (userModesPresent) {
            List<String> userModes = getUserModes(exchange);
            validateTokenRequest.setAdditionalKeys(additionalFields);
            validateTokenRequest.setUserModes(userModes);
        } else {
            validateTokenRequest.setAdditionalKeys(additionalFields);
            validateTokenRequest.setUserModes(Arrays.asList("Consumer_Guest", "CorporateGuest"));
        }
        return validateTokenRequest;
    }

    private List<String> getUserModes(ServerWebExchange exchange) {
        return exchange.getRequest().getQueryParams().get(Constants.USER_MODES);
    }

    private String getUriEncodedToken(ServerWebExchange exchange) {
        String header = CollectionUtils.isNotEmpty(exchange.getRequest().getHeaders().get(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME)) ?
                exchange.getRequest().getHeaders().get(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME).get(0) : null;
        if (header == null) {
            header = CollectionUtils.isNotEmpty(exchange.getRequest().getHeaders().get(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK)) ?
                    exchange.getRequest().getHeaders().get(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK).get(0) : null;
            if (header == null) {
                return null;
            }
        }
        return header;
    }

    private static Boolean isUserModesPresent(ServerWebExchange exchange) {
        return Objects.nonNull(exchange.getRequest().getQueryParams())
                && Objects.nonNull(exchange.getRequest().getQueryParams().get(Constants.USER_MODES));
    }
}
