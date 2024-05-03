package io.dizme.idp.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.upokecenter.cbor.CBORObject;
import io.dizme.idp.SSIEndpoint;
import io.dizme.idp.models.CredentialElement;
import io.dizme.idp.models.Document;
import org.jboss.logging.Logger;
import org.keycloak.common.util.Base64Url;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class CBORDecoder {
    protected static final Logger logger = Logger.getLogger(SSIEndpoint.class);

    public static List<CredentialElement> decodeCBOR(String vpToken, String credentialType) throws Exception {
        logger.debug("Decoding Base64 VP Token to bytes...");
        byte[] data = Base64.getUrlDecoder().decode(vpToken);
        logger.debug("Bytes created from Base64Url string.");

        logger.debug("Converting into JSON and printing");
        String jsonString = null;
        try {
            jsonString = cborToJson(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("JSON: "+jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<CredentialElement> elements = new ArrayList<>(); // List to hold the CredentialElements
        try {
            Document document = objectMapper.readValue(jsonString, Document.class);
            logger.debug("Document Version: " + document.getVersion());
            document.getDocuments().forEach(doc -> {
                List<String> credentialNamespace = doc.getIssuerSigned().getNameSpaces().get(credentialType);
                if (credentialNamespace != null) {
                    credentialNamespace.forEach(namespace -> {
                        byte[] namespaceAsByte = Base64Url.decode(namespace);
                        String nameSpaceJson = null;
                        try {
                            nameSpaceJson = cborToJson(namespaceAsByte);
                            ObjectMapper elementMapper = new ObjectMapper();
                            elementMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            CredentialElement element = elementMapper.readValue(nameSpaceJson, CredentialElement.class);
                            elements.add(element);  // Add the parsed element to the list
                            logger.debug(element);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elements; // Return the list of CredentialElements
    }

    protected static String cborToJson(byte[] input) throws IOException {
        CBORFactory cborFactory = new CBORFactory();
        CBORParser cborParser = cborFactory.createParser(input);
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        while (cborParser.nextToken() != null) {
            jsonGenerator.copyCurrentEvent(cborParser);
        }
        jsonGenerator.flush();
        return stringWriter.toString();
    }

}
