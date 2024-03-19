package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PathNested {
    public String format;
    public String path;

    // Getters and setters
}
