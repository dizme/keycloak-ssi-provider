package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyResult {
    @JsonProperty("policies")
    public List<Policy> policies;

    @JsonProperty("credential")
    public String credential;
}
