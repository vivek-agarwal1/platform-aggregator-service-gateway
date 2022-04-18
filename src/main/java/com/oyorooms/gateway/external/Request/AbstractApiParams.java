package com.oyorooms.gateway.external.Request;

import com.oyorooms.gateway.core.util.LoggerUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public abstract class AbstractApiParams implements Serializable {

    /**
     * Every implementing class should override this.
     * This method will be called in order to make the uri before making the actual http request
     * For normal use cases you can use the baseQueryMethod
     * @return
     */
    public String convertToQueryString(){
        return baseQueryMethod(this);
    };

    protected <T extends AbstractApiParams> String baseQueryMethod ( T obj ){

        StringBuilder sb = new StringBuilder();
        String queryString = null;
        Field[] fields = obj.getClass().getDeclaredFields();

        for ( Field field : fields ){
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (Objects.nonNull(value) && !field.isSynthetic()) {
                    sb.append(field.getName()).append("=").append(value).append("&");
                }
            } catch (IllegalAccessException e) {
                LoggerUtil.error( "Illegal Access Exception in Query conversion", e);
            }
        }
        if ( sb.length()> 0) {
            queryString = sb.substring(0, sb.length()-1);
        }
        return queryString;
    }
}
