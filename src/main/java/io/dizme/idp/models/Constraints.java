package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Constraints {
    public List<Field> fields;

    // Getters and setters
}
