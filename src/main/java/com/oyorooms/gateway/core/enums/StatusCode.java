package com.oyorooms.gateway.core.enums;

import com.oyorooms.gateway.helper.Constants;
import com.oyorooms.gateway.helper.ErrorConstants;
import org.springframework.http.HttpStatus;

public enum StatusCode {
    OK(200, ErrorConstants.Messages.BAD_REQUEST),
    UNAUTHORIZED(401, ErrorConstants.Messages.AUTHENTICATION_ERROR),
    BAD_REQUEST(400, ErrorConstants.Messages.BAD_REQUEST);

    private final Integer value;

    private final String msg;

    StatusCode(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static StatusCode getName(int value) throws Exception {
        switch (value) {
            case 0:
                return StatusCode.OK;
            case 1:
                return StatusCode.BAD_REQUEST;
            default:
                throw new Exception("Invalid StatusCode value: " + value);
        }
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatusCode() {
        switch (this) {
            case OK:
                return HttpStatus.OK;
            case UNAUTHORIZED:
                return HttpStatus.UNAUTHORIZED;
            case BAD_REQUEST:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }

    public String getType() {
        switch (this) {
            case UNAUTHORIZED:
                return ErrorConstants.Messages.AUTH_ERROR_TYPE;
            case BAD_REQUEST:
                return ErrorConstants.Messages.BAD_REQUEST;
            default:
                return Constants.ERROR_LOGGING_LABEL;
        }
    }
}
