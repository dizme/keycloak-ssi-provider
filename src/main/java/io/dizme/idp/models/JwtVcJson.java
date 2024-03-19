package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtVcJson {
    public List<String> alg;

    // Getters and setters
}
