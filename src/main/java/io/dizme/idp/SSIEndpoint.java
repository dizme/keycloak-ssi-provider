package io.dizme.idp;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityProvider;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.saml.common.constants.GeneralConstants;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.sessions.AuthenticationSessionModel;

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
        return execute(username, id, state);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postBinding(@FormParam("username") String username,
                                @FormParam("id") String id,
                                @FormParam("state") String state) {
        return execute(username, id, state);
    }

    @Path("clients/{client_id}")
    @GET
    public Response redirectBinding(@QueryParam("username") String username,
                                    @QueryParam("id") String id,
                                    @QueryParam("state") String state,
                                    @PathParam("client_id") String clientId)  {
        return execute(username, id, state);
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
        return execute(username, id, state);
    }

    private Response execute(String username, String id, String state) {
        logger.debug("username: " + username);
        logger.debug("id: " + id);
        logger.debug("state: " + state);

        BrokeredIdentityContext identity = new BrokeredIdentityContext(username);
        identity.setUsername(username);
        identity.setModelUsername(username);
        identity.setEmail("test@dizme.io");
        identity.setUserAttribute("username_attr", username);
        identity.setUserAttribute("id_attr", id);

        String brokerUserId = config.getAlias() + "." + username;
        identity.setBrokerUserId(brokerUserId);

        identity.setIdpConfig(config);
        identity.setIdp(provider);
        AuthenticationSessionModel authSession = callback.getAndVerifyAuthenticationSession(state);
        session.getContext().setAuthenticationSession(authSession);
        identity.setAuthenticationSession(authSession);

        return callback.authenticated(identity);
    }
}
