package com.oyorooms.gateway.core.enums;

import lombok.Getter;

@Getter
public enum PreFilterCustomEnum {
    AuthenticationFilterAndApplySignature("AuthenticationFilterAndApplySignature"),
    UnauthorizedRequestFilter("UnauthorizedRequestFilter"),
    RemoveAccessTokenFilter("RemoveAccessTokenFilter");

    private String name;

    private PreFilterCustomEnum(final String name) {
        this.name = name;
    }

}
