package com.oyorooms.gateway.core.enums;

import lombok.Getter;

@Getter
public enum PreFilterGlobalEnum {
    UriPartialEncodingToFullEncodingFilter("UriPartialEncodingToFullEncodingFilter");

    private String name;

    private PreFilterGlobalEnum(final String name) {
        this.name = name;
    }

}
