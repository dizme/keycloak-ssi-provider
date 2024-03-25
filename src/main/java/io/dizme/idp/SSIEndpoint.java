package io.dizme.idp;

import com.authlete.sd.Disclosure;
import com.authlete.sd.SDJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dizme.idp.models.VerificationSessionInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

import java.io.IOException;
import java.util.Arrays;
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
    //
    private static final String[] SPID_LEVEL= {"https://www.spid.gov.it/SpidL1", "https://www.spid.gov.it/SpidL2", "https://www.spid.gov.it/SpidL3"};

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
                                    @QueryParam("state") String state)  {
        return execute(id, state);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postBinding(@FormParam("username") String username,
                                @FormParam("id") String id,
                                @FormParam("state") String state) {
        return execute(id, state);
    }

    @Path("clients/{client_id}")
    @GET
    public Response redirectBinding(@QueryParam("username") String username,
                                    @QueryParam("id") String id,
                                    @QueryParam("state") String state,
                                    @PathParam("client_id") String clientId)  {
        return execute(id, state);
    }


    /**
     */
    @Path("clients/{client_id}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postBinding(@FormParam("username") String username,
                                @FormParam("id") String id,
                                @FormParam("state") String state,
                                @PathParam("client_id") String clientId) {
        return execute(id, state);
    }

    private Response execute(String id, String state) {
        logger.debug("Verification id from SSI Idp: " + id);
        try {
            VerificationSessionInfo sessionInfo = getTokenResponse(id);
            String verifiableCredential = sessionInfo.policyResults.results.get(0).policies.get(0).result.vp.verifiableCredentials.get(0);
//            logger.debug("Verifiable Credential: " + verifiableCredential);

            BrokeredIdentityContext identity = new BrokeredIdentityContext(id);
            identity.setUsername(id);
            identity.setModelUsername(id);
            identity.setEmail("test@dizme.io");

            String brokerUserId = config.getAlias() + "." + id;
            identity.setBrokerUserId(brokerUserId);

            identity.setIdpConfig(config);
            identity.setIdp(provider);
            AuthenticationSessionModel authSession = callback.getAndVerifyAuthenticationSession(state);
            session.getContext().setAuthenticationSession(authSession);
            identity.setAuthenticationSession(authSession);

            // Add User attribute from disclosed claims
            SDJWT sdJwt = parseFixed(verifiableCredential);
            sdJwt.getDisclosures().forEach(disclosure -> {
                identity.setUserAttribute(config.getCredentialType()+"_"+disclosure.getClaimName(), disclosure.getClaimValue().toString());
//                logger.debug("Adding user attribute: " + config.getCredentialType()+"_"+disclosure.getClaimName() + " with value: " + disclosure.getClaimValue().toString());
            });


            return callback.authenticated(identity);
        } catch (IllegalArgumentException iae) {
            logger.error("Error parsing SDJWT", iae);
            return callback.error(Response.Status.BAD_REQUEST.toString() + ": No claim found in the credential disclosure");
        } catch (Exception e) {
            logger.error("Error retrieving VP token", e);
            return callback.error(Response.Status.INTERNAL_SERVER_ERROR.toString());
        }

    }

    private VerificationSessionInfo getTokenResponse(String id) throws Exception {
        try(CloseableHttpClient client = HttpClients.createDefault();) {
            HttpGet request = new HttpGet(config.getVerifierUrl() + "/openid4vc/session/" + id);
            try (CloseableHttpResponse response = client.execute(request);) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Error retrieving VP token");
                }
                String responseBody = EntityUtils.toString(response.getEntity());
//                logger.debug("VP token: " + responseBody);

                ObjectMapper mapper = new ObjectMapper();

                // Parse JSON to ResponseObject
                return mapper.readValue(responseBody, VerificationSessionInfo.class);
            }
        }
    }

    public static SDJWT parseFixed(String input) {
        if (input == null) {
            return null;
        } else {
            String[] elements = input.split("~", -1);
            int lastIndex = elements.length - 1;

            for(int i = 0; i < lastIndex; ++i) {
                if (elements[i].isEmpty()) {
                    throw new IllegalArgumentException("The SD-JWT is malformed.");
                }
            }

            if (elements.length < 2) {
                throw new IllegalArgumentException("The SD-JWT is malformed.");
            } else {
                String credentialJwt = elements[0];
                String bindingJwt = input.endsWith("~") ? null : elements[lastIndex];

                List disclosures;
                try {
                    disclosures = (List) Arrays.asList(elements).subList(1, elements.length).stream().map(Disclosure::parse).collect(Collectors.toList());
                } catch (Exception var7) {
                    throw new IllegalArgumentException("Failed to parse disclosures.", var7);
                }

                return new SDJWT(credentialJwt, disclosures, bindingJwt);
            }
        }
    }
}