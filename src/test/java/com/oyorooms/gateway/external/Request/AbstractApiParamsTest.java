package com.oyorooms.gateway.external.Request;

import com.oyorooms.gateway.testClass.TestAbstractApiParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractApiParamsTest {

    @Test
    void convertToQueryStringWithNullObject() {
        TestAbstractApiParams abstractApiParams = new TestAbstractApiParams();
        assert(Objects.isNull(abstractApiParams.convertToQueryString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"param1", "param2", "param3"})
    void convertToQueryStringWithNonNullSingleFields(String key) throws NoSuchFieldException, IllegalAccessException {
        String expectedResult = key + "=" + key;
        TestAbstractApiParams abstractApiParams = new TestAbstractApiParams();
        abstractApiParams = setFieldInTestAbstractApiParams(abstractApiParams, key);

        assert(expectedResult.equals(abstractApiParams.convertToQueryString()));
    }

    @ParameterizedTest
    @CsvSource(value = {"param1, param2", "param2, param3", "param1, param3"})
    void convertToQueryStringWithNonNullPairFields(String key1, String key2) throws NoSuchFieldException, IllegalAccessException {
        TestAbstractApiParams abstractApiParams = new TestAbstractApiParams();
        setFieldInTestAbstractApiParams(abstractApiParams, key1);
        setFieldInTestAbstractApiParams(abstractApiParams, key2);

        String expectedResult = key1 + "=" + key1 + "&" + key2 + "=" + key2;
        assert(expectedResult.equals(abstractApiParams.convertToQueryString()));
    }

    @Test
    void convertToQueryStringWithAllNonNull() throws NoSuchFieldException, IllegalAccessException {
        TestAbstractApiParams abstractApiParams = new TestAbstractApiParams();
        setFieldInTestAbstractApiParams(abstractApiParams, "param1");
        setFieldInTestAbstractApiParams(abstractApiParams, "param2");
        setFieldInTestAbstractApiParams(abstractApiParams, "param3");

        String expectedResult = "param1" + "=" + "param1" + "&"
                + "param2" + "=" + "param2" + "&"
                + "param3" + "=" + "param3";
        assert(expectedResult.equals(abstractApiParams.convertToQueryString()));
    }

    private TestAbstractApiParams setFieldInTestAbstractApiParams(TestAbstractApiParams abstractApiParams, String key) throws NoSuchFieldException, IllegalAccessException {
        Field field = abstractApiParams.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(abstractApiParams, key);

        return abstractApiParams;
    }
}