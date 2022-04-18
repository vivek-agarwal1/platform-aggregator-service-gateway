//package com.oyorooms.gateway.core.util;
//
//import com.oyorooms.gateway.testClass.TestAbstractApiParams;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class JWTSignatureUtilTest {
//
//    @Test
//    void getJwtSignatureWithObjectWithoutAnySerializer() {
//        assertThrows(IllegalArgumentException.class,
//                () -> JWTSignatureUtil.getJwtSignature("mock-test-key", new Object()));
//    }
//
//    @Test
//    void getJwtSignatureWithObjectWithSerializerWithEnvironmentHS512_signature_key() {
//        assertDoesNotThrow(
//                () -> JWTSignatureUtil.getJwtSignature("mock-test-key", new TestAbstractApiParams()));
//    }
//
//}