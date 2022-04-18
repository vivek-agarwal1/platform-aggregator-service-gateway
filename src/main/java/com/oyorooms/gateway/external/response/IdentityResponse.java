package com.oyorooms.gateway.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityResponse extends BaseExternalResponse{

    @JsonProperty("auth_valid")
    private Boolean authValid;

    @JsonProperty("authorized")
    private Boolean appAuthorized;

    @JsonProperty("app_client")
    private AppClientDetails appClient;

    @JsonProperty("user")
    private LoggedInUserDetails loggedInUserDetails;

    @JsonProperty("context_users")
    private Map<String, LoggedInUserDetails> contextUserResponse;

    @JsonProperty("user_mode_last_updated_timestamp")
    private Map<String, Long> userModeLastUpdated;

    public boolean isUserValid() {
        if(authValid && !ObjectUtils.isEmpty(appClient)){
            return true;
        }
        return false;
    }
}
