package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationSessionInfo {
    public String id;
    @JsonProperty("presentationDefinition")
    public PresentationDefinition presentationDefinition;
    @JsonProperty("tokenResponse")
    public TokenResponse tokenResponse;
    @JsonProperty("verificationResult")
    public boolean verificationResult;
    @JsonProperty("policyResults")
    public PolicyResultObject policyResults;

    // Getters and setters
}
