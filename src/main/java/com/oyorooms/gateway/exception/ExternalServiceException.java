package com.oyorooms.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyorooms.gateway.core.util.ObjectMapperUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalServiceException extends Exception {

    private static ObjectMapper mapper = ObjectMapperUtil.getCustomObjectMapper();
    private final HttpStatus errorCode;

    public ExternalServiceException(String message) {
        super(message);
        errorCode = HttpStatus.BAD_GATEWAY;
    }

    public ExternalServiceException() {
        super(HttpStatus.BAD_GATEWAY.getReasonPhrase());
        errorCode = HttpStatus.BAD_GATEWAY;
    }

    public ExternalServiceException(Throwable e, HttpStatus httpStatus) {
        super(e);
        errorCode = httpStatus;
    }

    public ExternalServiceException(String message, HttpStatus httpStatus) {
        super(message);
        errorCode = httpStatus;
    }

    public boolean is4xxClientError() {
        return errorCode != null && errorCode.is4xxClientError();
    }

}
