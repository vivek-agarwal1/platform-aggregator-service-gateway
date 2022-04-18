package com.oyorooms.gateway.testClass;

import com.oyorooms.gateway.external.Request.AbstractApiParams;
import lombok.Data;

@Data
public class TestAbstractApiParams extends AbstractApiParams {

    private String param1;
    private String param2;
    private String param3;

    @Override
    public String convertToQueryString() {
        return super.convertToQueryString();
    }
}
