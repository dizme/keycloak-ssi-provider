package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    @JsonProperty("vp_token")
    public String vpToken;

    @JsonProperty("presentation_submission")
    public PresentationSubmission presentationSubmission;

    public String state;

    // Getters and setters
}
