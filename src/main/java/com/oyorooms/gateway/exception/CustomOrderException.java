package com.oyorooms.gateway.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomOrderException extends Exception {

    private static final long serialVersionUID = -7478839340745881381L;

    private final HttpStatus statusCode;

    private Integer code;
    private String type;
    private String message;
    private String errorMessage;

    public CustomOrderException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.type = "error";
    }

    public CustomOrderException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomOrderException(Throwable e, HttpStatus httpStatus) {
        super(e);
        statusCode = httpStatus;
    }

    public CustomOrderException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.statusCode = httpStatus;
    }

    public CustomOrderException(String message, String errorMessage, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.errorMessage = errorMessage;
        this.statusCode = httpStatus;
    }

    public CustomOrderException(Integer code, String type, String message, HttpStatus httpStatus) {
        super(message);
        this.statusCode = httpStatus;
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public CustomOrderException(Integer code, String type, String message, String errorMessage, HttpStatus httpStatus) {
        super(message);
        this.statusCode = httpStatus;
        this.code = code;
        this.type = type;
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public CustomOrderException(Integer code, String type, HttpStatus httpStatus) {
        super();
        this.statusCode = httpStatus;
        this.code = code;
        this.type = type;
    }

    public CustomOrderException(Integer code, HttpStatus httpStatus, String message) {
        super(message);
        this.statusCode = httpStatus;
        this.code = code;
        this.message = message;
    }
}
