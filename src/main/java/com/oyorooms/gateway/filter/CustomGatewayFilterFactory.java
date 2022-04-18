package com.oyorooms.gateway.filter;

import com.oyorooms.gateway.filter.customFilter.AuthenticationFilterAndApplySignature;
import com.oyorooms.gateway.filter.customFilter.RemoveAccessTokenFilter;
import com.oyorooms.gateway.filter.customFilter.UnauthorizedRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomGatewayFilterFactory {

    @Autowired
    private AuthenticationFilterAndApplySignature authenticationFilter;

    @Autowired
    private UnauthorizedRequestFilter unauthorizedRequestFilter;

    @Autowired
    private RemoveAccessTokenFilter removeAccessTokenFilter;

    public AbstractGatewayFilterFactory getCustomGatewayFilter(String className){
        switch (className){
            case "AuthenticationFilter":
                return authenticationFilter;
            case "UnauthorizedCheckFilter":
                return unauthorizedRequestFilter;
            case "RemoveAccessTokenFilter":
                return removeAccessTokenFilter;
            default:
                return null;
        }
    }
}
