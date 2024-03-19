package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VP {
    @JsonProperty("holder")
    public String holder;
    @JsonProperty("verifiableCredential")
    public List<String> verifiableCredentials;
}
