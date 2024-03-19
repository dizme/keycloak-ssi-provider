package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PresentationSubmission {
    public String id;
    @JsonProperty("definition_id")
    public String definitionId;
    @JsonProperty("descriptor_map")
    public List<DescriptorMap> descriptorMap;

    // Getters and setters
}
