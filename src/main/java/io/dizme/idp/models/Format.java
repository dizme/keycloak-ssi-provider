package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Format {
    @JsonProperty("jwt_vc_json")
    public JwtVcJson jwtVcJson;

    // Getters and setters
}
