package io.dizme.idp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PresentationDefinition {
    @JsonProperty("input_descriptors")
    public List<InputDescriptor> inputDescriptors;

    // Getters and setters
}

