package com.oyorooms.gateway.helper;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Constants {

    public static final String LOG_CACHE_KEYNAME = "cache_key";

    public static final String API_URL_KEY = "api_url";

    public static final String API_BODY_KEY = "api_body";

    public static final String ERROR_MESSAGE_KEY = "error_msg";

    public static final String STACKTRACE_KEY = "trace";

    public static final String ORDER_GATEWAY_SERVICE = "order-gateway-service";

    public static final String ACCESS_TOKEN = "access-token";

    public static final String CRS = "crs";

    public static final String ADDITIONAL_KEYS = "additional_keys";

    public static final String USER_MODES = "user_mode";

    public static final List<String> ADDITIONAL_FIELDS = Arrays.asList(Constants.USER_MODES);

    public static final String AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME = "access_token";

    public static final String AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK = "access-token";

    public static final String VALIDATE_TOKEN_RESPONSE = "validateTokenResponse";

    public static final String GATEWAY_TOKEN = "gateway-token";

    public static final String GATEWAY_CLIENT_IDENTIFIER = "gatewayClient";

    public static final String ERROR_LOGGING_LABEL = "Error";
}
