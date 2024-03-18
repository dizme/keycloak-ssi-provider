package io.dizme.idp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.keycloak.broker.provider.*;
import org.keycloak.common.util.Base64;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.sessions.AuthenticationSessionModel;
import twitter4j.RequestToken;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SSIIdentityProvider extends AbstractIdentityProvider<SSIIdentityProviderConfig> {

    protected static final Logger logger = Logger.getLogger(SSIIdentityProvider.class);

    private final DestinationValidator destinationValidator;

    private final String verifierUrl;

    private final String credentialType;

    public SSIIdentityProvider(KeycloakSession session, SSIIdentityProviderConfig config, DestinationValidator destinationValidator) {
        super(session, config);
        this.destinationValidator = destinationValidator;
        this.verifierUrl = config.getVerifierUrl();
        this.credentialType = config.getCredentialType();
        logger.debug("SSIIdentityProvider() called");

    }

    @Override
    public Object callback(RealmModel realm, AuthenticationCallback callback, EventBuilder event) {
        logger.debug("callback called");
        // TODO: Define usermodel based on sessionid received in query param Or cookie
        // session.users().addUser(realm, "user");
//        String username = session.getContext().getHttpRequest().getUri().getQueryParameters().get("username").get(0);
//        String id = session.getContext().getHttpRequest().getUri().getQueryParameters().get("id").get(0);
//        String state = session.getContext().getHttpRequest().getUri().getQueryParameters().get("state").get(0);

        return new SSIEndpoint(session, this, getConfig(), callback, destinationValidator);
    }

    @Override
    public Response performLogin(AuthenticationRequest request) {
        logger.debug("performLogin called");
//        try {
////            Perform redirect verso l'idp
////            Response.seeOther(new URI(verifierUrl+"/openid4vc/verify")).build();
////            TODO: study options
////            Restituire html con form auto submit
//            URI uri = new URI(verifierUrl+"/openid4vc/verify");
////            Creare la callback
//            String redirectUrl = request.getRedirectUri();
//            return Response.seeOther(new URI(verifierUrl+"?redirectUrl="+redirectUrl)).build();
//        } catch (Exception e) {
//            throw new IdentityBrokerException("Could send authentication request.", e);
//        }

        try {
            session.setAttribute(
                    "callbackUri",
                    request.getRedirectUri()
            );
            URI uri = new URI(request.getRedirectUri() + "?state=" + request.getState().getEncoded());
            String uriEncoded = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8);
            return Response.seeOther(URI.create(verifierUrl + "?credentialType=" + credentialType + "&redirectUri=" + uriEncoded)).build();
        } catch (Exception e) {
            throw new IdentityBrokerException("Could send authentication request to twitter.", e);
        }
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

//    @Override
//    public boolean reloadKeys() {
//        return super.reloadKeys();
//    }
}
