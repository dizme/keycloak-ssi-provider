package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptorMap {
    public String format;
    public String path;
    @JsonProperty("path_nested")
    public PathNested pathNested;

    // Getters and setters
}
