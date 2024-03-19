package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {
    public List<String> path;
    public Filter filter;

    // Getters and setters
}
