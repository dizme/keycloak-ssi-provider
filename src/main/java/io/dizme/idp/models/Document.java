package io.dizme.idp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Document {
    private String version;
    private List<Doc> documents;
    private int status;

    // Getters and Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Doc> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Doc> documents) {
        this.documents = documents;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Doc {
        @JsonProperty("issuerSigned")
        private IssuerSigned issuerSigned;

        public IssuerSigned getIssuerSigned() {
            return issuerSigned;
        }

        public void setIssuerSigned(IssuerSigned issuerSigned) {
            this.issuerSigned = issuerSigned;
        }
    }

    public static class IssuerSigned {
        @JsonProperty("nameSpaces")
        private Map<String, List<String>> nameSpaces;
        @JsonProperty("issuerAuth")
        private List<Object> issuerAuth;

        // Getters and Setters
        public Map<String, List<String>> getNameSpaces() {
            return nameSpaces;
        }

        public void setNameSpaces(Map<String, List<String>> nameSpaces) {
            this.nameSpaces = nameSpaces;
        }

        public List<Object> getIssuerAuth() {
            return issuerAuth;
        }

        public void setIssuerAuth(List<Object> issuerAuth) {
            this.issuerAuth = issuerAuth;
        }
    }
}
