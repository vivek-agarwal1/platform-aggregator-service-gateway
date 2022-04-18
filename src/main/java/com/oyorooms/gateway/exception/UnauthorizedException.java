package com.oyorooms.gateway.exception;

import com.oyorooms.gateway.core.enums.StatusCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UnauthorizedException extends CustomOrderException{
    public UnauthorizedException(Integer code, String type, String message){
        super(code, type, message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(){
        super(10, StatusCode.UNAUTHORIZED.getType(), StatusCode.UNAUTHORIZED.getMsg(), StatusCode.UNAUTHORIZED.getStatusCode());
    }
}
