package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Filter {
    public String type;
    public String pattern;

    // Getters and setters
}
