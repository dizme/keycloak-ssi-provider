package io.dizme.idp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dizme.idp.exceptions.InvalidVpTokenException;
import io.dizme.idp.models.CredentialElement;
import io.dizme.idp.models.TokenResponse;
import io.dizme.idp.utils.CBORDecoder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityProvider;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.sessions.AuthenticationSessionModel;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.List;
import java.util.stream.Collectors;

public class SSIEndpoint {
    protected static final Logger logger = Logger.getLogger(SSIEndpoint.class);
    protected RealmModel realm;
    protected EventBuilder event;
    protected SSIIdentityProviderConfig config;
    protected IdentityProvider.AuthenticationCallback callback;
    protected SSIIdentityProvider provider;
    private final DestinationValidator destinationValidator;
    // iso8601 fully compliant regex
    private static final String _UTC_STRING = "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])?$";

    @Context
    private KeycloakSession session;

    @Context
    private ClientConnection clientConnection;

    @Context
    private HttpHeaders headers;
    public SSIEndpoint(KeycloakSession session, SSIIdentityProvider ssiIdentityProvider, SSIIdentityProviderConfig config, IdentityProvider.AuthenticationCallback callback, DestinationValidator destinationValidator) {
        this.realm = session.getContext().getRealm();
        this.config = config;
        this.callback = callback;
        this.provider = ssiIdentityProvider;
        this.destinationValidator = destinationValidator;
        this.session = session;
        this.clientConnection = session.getContext().getConnection();
        this.headers = session.getContext().getRequestHeaders();
    }

    @GET
    @NoCache
    @Path("descriptor")
    public Response getSPDescriptor() {
        return provider.export(session.getContext().getUri(), realm, null);
    }


    @GET
    public Response redirectBinding(@QueryParam("username") String username,
                                    @QueryParam("id") String id,
                                    @QueryParam("state") String state) {
        return execute(id, state);
    }

    @Path("clients/{client_id}")
    @GET
    public Response redirectBinding(@QueryParam("username") String username,
                                    @QueryParam("id") String id,
                                    @QueryParam("state") String state,
                                    @PathParam("client_id") String clientId) {
        return execute(id, state);
    }


    private Response execute(String id, String state) {
        logger.debug("Verification id from SSI Idp: " + id);
        try {
            String vpToken = getTokenResponse(id);
            List<CredentialElement> elements = CBORDecoder.decodeCBOR(vpToken, config.getCredentialType());
            if (elements.isEmpty()) {
                throw new InvalidVpTokenException("No claim found in vp token");
            }
            BrokeredIdentityContext identity = new BrokeredIdentityContext(id);
            List<CredentialElement> result = elements.stream()
                    .filter(item -> item.getElementIdentifier().equals("document_number"))
                    .collect(Collectors.toList());
            String idLabel = result.isEmpty() ? id : result.get(0).getElementValue();
            identity.setUsername(idLabel);
            identity.setModelUsername(idLabel);
            identity.setEmail("test@dizme.io");

            String brokerUserId = config.getAlias() + "." + idLabel;
            identity.setBrokerUserId(brokerUserId);

            identity.setIdpConfig(config);
            identity.setIdp(provider);
            AuthenticationSessionModel authSession = callback.getAndVerifyAuthenticationSession(state);
            session.getContext().setAuthenticationSession(authSession);
            identity.setAuthenticationSession(authSession);

            elements.forEach(
                    element -> identity.setUserAttribute(config.getCredentialType()+"_"+element.getElementIdentifier(), element.getElementValue())
            );

            return callback.authenticated(identity);
        } catch (InvalidVpTokenException ivpte) {
            logger.error("Error parsing VpToken", ivpte);
            return callback.error(Response.Status.BAD_REQUEST + ": " +ivpte.getMessage());
        } catch (Exception e) {
            logger.error("Error retrieving VP token", e);
            return callback.error(Response.Status.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
        }

    }

    private String getTokenResponse(String id) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
//        try (CloseableHttpClient client = createHttpClient()) {
            HttpGet request = new HttpGet(config.getVerifierUrl() + "/ui/presentations/" + id);
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Error retrieving VP token");
                }
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.debug("VP token response: " + responseBody);

                // Deserialize JSON response to TokenResponse class
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                TokenResponse tokenResponse = mapper.readValue(responseBody, TokenResponse.class);

                // Retrieve the vp_token from the TokenResponse object
                String vpToken = tokenResponse.getVpToken();
                logger.debug("Extracted VP token: " + vpToken);
                return vpToken;  // Return the vp_token
            }
        }
    }

    private CloseableHttpClient createHttpClient() throws Exception {
        // Load the key store
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(new File("/Users/dariocastellano/Repository/keycloak-repos/verifier-backend.jks"))) {
            keyStore.load(instream, "changeit".toCharArray());
        }

        // Create SSL context
        SSLContextBuilder builder = SSLContextBuilder.create();
        builder.loadTrustMaterial(keyStore, new TrustSelfSignedStrategy());
        SSLContext sslContext = builder.build();

        // Create an HttpClient with the custom SSL context
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }



}

