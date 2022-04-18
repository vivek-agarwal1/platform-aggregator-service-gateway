//package com.oyorooms.gateway.core.util;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.oyo.tokenauthentication.entity.requests.JWTRequest;
//import com.oyo.tokenauthentication.enums.JWTStrategyType;
//import com.oyo.tokenauthentication.service.JWTAuthenticationService;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class JWTSignatureUtil {
//
//    static JWTAuthenticationService authenticationService = new JWTAuthenticationService(JWTStrategyType.SIGNATURE);
//
//    public static <T> String getJwtSignature(String key, T value){
//        Map<String, Object> claimsJwtRequest = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        claimsJwtRequest.put(key, objectMapper.valueToTree(value));
//        JWTRequest jwtRequest = JWTRequest.builder()
//                .claims(claimsJwtRequest)
//                .build();
//        return authenticationService.generateToken(jwtRequest);
//    }
//}
