package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty("vp_token")
    private String vpToken;

    public String getVpToken() {
        return vpToken;
    }

    public void setVpToken(String vp_token) {
        this.vpToken = vp_token;
    }
}
