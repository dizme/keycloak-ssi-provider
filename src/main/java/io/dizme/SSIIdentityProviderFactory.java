package io.dizme;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ConfiguredProvider;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.saml.validators.DestinationValidator;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SSIIdentityProviderFactory extends AbstractIdentityProviderFactory<SSIIdentityProvider> implements ConfiguredProvider {
    protected static final Logger logger = Logger.getLogger(SSIIdentityProviderFactory.class);

    public static final String PROVIDER_ID = "ssi-oidc";

    private DestinationValidator destinationValidator;



    @Override
    public String getName() {
        logger.debug("getName called");
        return "SSI OIDC";
    }

    @Override
    public SSIIdentityProvider create(KeycloakSession keycloakSession, IdentityProviderModel identityProviderModel) {
        logger.debug("create called");
        identityProviderModel.getConfig();
        return new SSIIdentityProvider(keycloakSession, new SSIIdentityProviderConfig(identityProviderModel), destinationValidator);
    }

    @Override
    public IdentityProviderModel createConfig() {
        logger.debug("createConfig called");
        return new SSIIdentityProviderConfig();
    }

    @Override
    public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
        logger.debug("parseConfig called");
        return super.parseConfig(session, inputStream);
    }

    @Override
    public String getId() {
        logger.debug("getId called");
        return PROVIDER_ID;
    }

    @Override
    public void init(Config.Scope config) {
        logger.debug("init called");
        super.init(config);
        this.destinationValidator = DestinationValidator.forProtocolMap(config.getArray("knownProtocols"));
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        logger.debug("getConfigProperties called");
        return SSIIdentityProviderConfig.getConfigProperties();
    }
}
