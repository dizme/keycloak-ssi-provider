package io.dizme.idp;

import org.jboss.logging.Logger;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SSIIdentityProviderConfig extends IdentityProviderModel {
    protected static final Logger logger = Logger.getLogger(SSIIdentityProviderConfig.class);

    public static final String VERIFIER_URL = "verifierUrl";

    public static final String CREDENTIAL_TYPE = "credentialType";

    public static final String IDP_URL = "idpUrl";

    public static final String CLAIM_REQUESTED = "claimRequested";

    public SSIIdentityProviderConfig() {
        logger.info("SSIIdentityProviderConfig() called");
    }

    public SSIIdentityProviderConfig(IdentityProviderModel identityProviderModel) {
        super(identityProviderModel);

        logger.info("SSIIdentityProviderConfig(IdentityProviderModel identityProviderModel) called");
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

    public String getIdpUrl() {
        return getConfig().get(IDP_URL);
    }

    public void setIdpUrl(String idpUrl) {
        getConfig().put(IDP_URL, idpUrl);
    }

    public String getClaimRequested() {
        return getConfig().get(CLAIM_REQUESTED);
    }

    public static List<ProviderConfigProperty> getConfigProperties() {
        logger.info("getConfigProperties called");
        return ProviderConfigurationBuilder.create()

                .property()
                .name(IDP_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Idp Url")
                .helpText("Insert Idp Url")
                .add()

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

                .property()
                .name(CLAIM_REQUESTED)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Claim Requested")
                .helpText("The credential claims to ask for. It can be a comma-separated list of claims. No defaults.")
                .add()

                .build();
    }

    @Override
    public void setFirstBrokerLoginFlowId(String firstBrokerLoginFlowId) {
        super.setFirstBrokerLoginFlowId(null);
    }

    @Override
    public void setPostBrokerLoginFlowId(String postBrokerLoginFlowId) {
        super.setPostBrokerLoginFlowId(null);
    }
}
