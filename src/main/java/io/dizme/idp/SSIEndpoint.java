package io.dizme.idp;

import com.authlete.sd.Disclosure;
import com.authlete.sd.SDJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
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
                                    @QueryParam("state") String state,
                                    @Context UriInfo uriInfo) {
        return execute(id, state, uriInfo);
    }

    @Path("clients/{client_id}")
    @GET
    public Response redirectBinding(@QueryParam("username") String username,
                                    @QueryParam("id") String id,
                                    @QueryParam("state") String state,
                                    @PathParam("client_id") String clientId,
                                    @Context UriInfo uriInfo) {
        return execute(id, state, uriInfo);
    }


    private Response execute(String id, String state, UriInfo uriInfo) {
        logger.debug("Verification id from SSI Idp: " + id);
        // Access all query parameters
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        queryParams.forEach((key, value) -> logger.debug("AttirbuteKey: " + key + " AttributeValue: " + value));
        try {

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

            queryParams.forEach(
                    (key, value) -> identity.setUserAttribute(config.getCredentialType()+"_"+key, value.toString())
            );

            return callback.authenticated(identity);
        } catch (IllegalArgumentException iae) {
            logger.error("Error parsing SDJWT", iae);
            return callback.error(Response.Status.BAD_REQUEST.toString() + ": No claim found in the credential disclosure");
        } catch (Exception e) {
            logger.error("Error retrieving VP token", e);
            return callback.error(Response.Status.INTERNAL_SERVER_ERROR.toString());
        }

    }
}

