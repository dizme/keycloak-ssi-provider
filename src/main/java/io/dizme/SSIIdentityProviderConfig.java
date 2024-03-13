package io.dizme;

import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;

public class SSIIdentityProviderConfig extends OIDCIdentityProviderConfig {
    protected static final Logger logger = Logger.getLogger(SSIIdentityProviderConfig.class);

    public static final String VERIFIER_URL = "verifierUrl";

    public static final String CREDENTIAL_TYPE = "credentialType";

    public SSIIdentityProviderConfig() {
        logger.debug("SSIIdentityProviderConfig() called");
    }

    public SSIIdentityProviderConfig(IdentityProviderModel identityProviderModel) {
        super(identityProviderModel);

        logger.debug("SSIIdentityProviderConfig(IdentityProviderModel identityProviderModel) called");
    }

    public String getVerifierUrl() {
        return getConfig().get(VERIFIER_URL);
    }

    public void setVerifierUrl(String verifierUrl) {
        getConfig().put(VERIFIER_URL, verifierUrl);
    }

    public String getCredentialType() {
        return getConfig().get(CREDENTIAL_TYPE);
    }

    public void setCredentialType(String credentialType) {
        getConfig().put(CREDENTIAL_TYPE, credentialType);
    }

    public static List<ProviderConfigProperty> getConfigProperties() {
        logger.debug("getConfigProperties called");
        return ProviderConfigurationBuilder.create()

                .property()
                .name(VERIFIER_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Verifier Url")
                .helpText("Insert Verifier Url")
                .add()

                .property()
                .name(CREDENTIAL_TYPE)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Credential Type")
                .helpText("Insert Credential Type")
                .add()

                .build();
    }



}
