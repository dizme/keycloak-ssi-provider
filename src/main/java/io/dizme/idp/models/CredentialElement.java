package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialElement {
    @JsonProperty("elementIdentifier")
    private String elementIdentifier;
    @JsonProperty("elementValue")
    private Object elementValue;

    public String getElementIdentifier() {
        return elementIdentifier;
    }

    public void setElementIdentifier(String elementIdentifier) {
        this.elementIdentifier = elementIdentifier;
    }

    public Object getElementValue() {
        return elementValue;
    }

    public void setElementValue(Object elementValue) {
        this.elementValue = elementValue;
    }

    @Override
    public String toString() {
        return "CredentialElement{" +
                "elementIdentifier='" + elementIdentifier + '\'' +
                ", elementValue=" + elementValue +
                '}';
    }
}
