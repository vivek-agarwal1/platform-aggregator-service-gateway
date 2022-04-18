package com.oyorooms.gateway.external.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oyorooms.gateway.helper.Constants;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateTokenRequest extends AbstractApiParams {

    @JsonProperty("auth_token")
    private String authToken;

    @JsonProperty("additional_keys")
    private List<String> additionalKeys;

    @JsonProperty("user_modes")
    private List<String> userModes;

    @JsonProperty("namespace")
    private String namespace = Constants.CRS;

    @Override
    public String convertToQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append("auth_token=").append(this.authToken).append("&").append("namespace=").append(this.namespace);

        if (CollectionUtils.isNotEmpty(this.getAdditionalKeys())) {
            sb.append("&").append(Constants.ADDITIONAL_KEYS).append("[]=").append(additionalKeys);
        }

        if (this.getUserModes() != null) {
            for (String s : userModes) {
                sb.append("&").append("user_modes[]=");
                sb.append(s);
            }
        }
        return sb.toString();
    }
}
