package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InputDescriptor {
    public String id;
    public Format format;
    public Constraints constraints;

    // Getters and setters
}
