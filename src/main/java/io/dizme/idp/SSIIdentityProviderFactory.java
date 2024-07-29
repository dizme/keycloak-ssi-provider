package io.dizme.idp;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ConfiguredProvider;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.saml.validators.DestinationValidator;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SSIIdentityProviderFactory extends AbstractIdentityProviderFactory<SSIIdentityProvider> implements ConfiguredProvider, ServerInfoAwareProviderFactory {
    protected static final Logger logger = Logger.getLogger(SSIIdentityProviderFactory.class);

    public static final String PROVIDER_ID = "ssi-provider";

    private DestinationValidator destinationValidator;



    @Override
    public String getName() {
        logger.info("getName called");
        return "SSI Provider";
    }

    @Override
    public SSIIdentityProvider create(KeycloakSession keycloakSession, IdentityProviderModel identityProviderModel) {
        logger.info("create called");
        return new SSIIdentityProvider(keycloakSession, new SSIIdentityProviderConfig(identityProviderModel), destinationValidator);
    }

    @Override
    public IdentityProviderModel createConfig() {
        logger.info("createConfig called");
        return new SSIIdentityProviderConfig();
    }

    @Override
    public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
        logger.info("parseConfig called");
        return super.parseConfig(session, inputStream);
    }

    @Override
    public String getId() {
        logger.info("getId called");
        return PROVIDER_ID;
    }

    @Override
    public void init(Config.Scope config) {
        logger.info("init called");
        super.init(config);
        this.destinationValidator = DestinationValidator.forProtocolMap(config.getArray("knownProtocols"));
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        logger.info("getConfigProperties called");
        return SSIIdentityProviderConfig.getConfigProperties();
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("provider-name", "SSI Provider");
        return ret;
    }
}
