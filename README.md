# Keycloak SSI Provider

This is a Keycloak provider that allows you to use a W3C SDJWT Credential to authenticate users in Keycloak.

## Build and run this repository (Recommended)
This is the recommended way to start using this repository, you can also configure it manually but in the future the manual configuration will be heavily edited

### Build SSI Plugin

1. clone this repository
2. run 'mvn clean package' in the root directory
3. copy the jar file from the target directory to the 'providers' directory in your Keycloak installation
4. restart Keycloak

### Launching supporting SSI IDP

This repository also contain a simple IDP provider implementation that uses [EUDI verifier endpoints](https://verifier-backend.eudiw.dev) as a Credential Verifier.
The wallet used for testing purposes is the [EUDI Wallet](https://github.com/eu-digital-identity-wallet/eudi-app-android-wallet-ui/tree/main)
The directory `idp` contains a docker-compose file that launches the IDP and the Keycloak server with the SSI Verifier provider installed.

1. Go to the `idp` directory
2. Run `docker-compose up -d`

### Launching KeyCloak with provided realm

This repository also contain a preconfigured keycloak deployment. To run:

1. Go to the `docker` directory
2. Run `docker-compose up -d`

## How to configure manually

The previous setup should already set everything you need. Follow this steps only to check that everything is correctly set.

1. Go to the Keycloak admin console `http://localhost:8080` logging in with `admin/password`
2. Go to the realm you want to configure (`pid-verification-realm` should be already present)
3. Go to the 'Identity Providers' tab
4. Click on 'Add provider' and select 'SSI Verifier'
5. Fill the `IDP Url` with the URL of the IDP you want to use (e.g. using provided idp `http://localhost:88`)
6. Fill the `Credential Type` with the Type of the credential you want to verify.
7. Click on 'Save'
8. Go to the 'Authentication' tab
9. Go to the 'Flows' tab
10. Select the flow you want to edit or copy (e.g. 'Browser')
11. Remove every step except from `Cookie` and `Identity Provider Redirector`
12. Add the 'ssi-verifier' execution as `Identity Provider Redirector -> Default provider` to the flow
13. Click on 'Save'
14. Select Action _Bind Flow_
15. Select the flow you want to use (e.g. 'Browser')
