package io.dizme;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.keycloak.broker.provider.AbstractIdentityProvider;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityProviderMapper;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.sessions.AuthenticationSessionModel;

public class SSIIdentityProvider extends AbstractIdentityProvider<SSIIdentityProviderConfig> {

    protected static final Logger logger = Logger.getLogger(SSIIdentityProvider.class);

    private final DestinationValidator destinationValidator;

    public SSIIdentityProvider(KeycloakSession session, SSIIdentityProviderConfig config, DestinationValidator destinationValidator) {
        super(session, config);
        this.destinationValidator = destinationValidator;
        logger.debug("SSIIdentityProvider() called");

    }

    @Override
    public Object callback(RealmModel realm, AuthenticationCallback callback, EventBuilder event) {
        logger.debug("callback called");
        return super.callback(realm, callback, event);
    }

    @Override
    public Response performLogin(AuthenticationRequest request) {
        logger.debug("performLogin called");
        return super.performLogin(request);
    }

    @Override
    public void authenticationFinished(AuthenticationSessionModel authSession, BrokeredIdentityContext context) {
        logger.debug("authenticationFinished called");
        super.authenticationFinished(authSession, context);
    }

    @Override
    public Response retrieveToken(KeycloakSession keycloakSession, FederatedIdentityModel federatedIdentityModel) {
        logger.debug("retrieveToken called");
        return Response.ok(federatedIdentityModel.getToken()).build();
    }

    @Override
    public Response keycloakInitiatedBrowserLogout(KeycloakSession session, UserSessionModel userSession, UriInfo uriInfo, RealmModel realm) {
        logger.debug("keycloakInitiatedBrowserLogout called");
        return super.keycloakInitiatedBrowserLogout(session, userSession, uriInfo, realm);
    }

    @Override
    public Response export(UriInfo uriInfo, RealmModel realm, String format) {
        logger.debug("export called");
        return super.export(uriInfo, realm, format);
    }

    @Override
    public boolean isMapperSupported(IdentityProviderMapper mapper) {
        return super.isMapperSupported(mapper);
    }

    @Override
    public boolean reloadKeys() {
        return super.reloadKeys();
    }
}
