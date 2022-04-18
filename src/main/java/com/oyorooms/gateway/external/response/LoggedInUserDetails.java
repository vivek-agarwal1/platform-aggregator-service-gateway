package com.oyorooms.gateway.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oyorooms.gateway.core.enums.HomeBrand;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LoggedInUserDetails implements Serializable {

    private static final long serialVersionUID = 6899111070786171496L;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_profile_id")
    private String profileId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    private String phone;

    @JsonProperty("country_id")
    private Integer countryId;

    @JsonProperty("currency_id")
    private Long currencyId;

    @JsonProperty("devise_role")
    private String deviseRole;

    @JsonProperty("phone_code")
    private String phoneCode;

    @JsonProperty("is_relationship_mode_on")
    private boolean isRelationshipModeOn;

    private String role;

    @JsonProperty("context_users")
    private Map<String, LoggedInUserDetails> contextUsers;

    @JsonProperty("ovh_user")
    private Boolean isOvhUser;

    private HomeBrand brand;
}
