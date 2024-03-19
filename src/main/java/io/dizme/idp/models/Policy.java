package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Policy {
    @JsonProperty("policy")
    public String policy;
    @JsonProperty("description")
    public String description;
    @JsonProperty("is_success")
    public boolean isSuccess;
    @JsonProperty("result")
    public Result result;

}
