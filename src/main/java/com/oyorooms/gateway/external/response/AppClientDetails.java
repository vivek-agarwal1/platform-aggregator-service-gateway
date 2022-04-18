package com.oyorooms.gateway.external.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppClientDetails implements Serializable {

    private static final long serialVersionUID = -3363936084822990204L;

    @JsonProperty("id")
    private Long clientId;

    @JsonProperty("team_name")
    private String teamName;

    private String appClientName;

    @JsonProperty("name")
    private String apiClient;

    @JsonProperty("tags")
    private List<String> tags;

}
