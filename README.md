# Keycloak SSI Provider

This is a Keycloak provider that allows you to use a W3C SDJWT Credential to authenticate users in Keycloak.

## Build

1. clone this repository
2. run 'mvn clean package' in the root directory
3. copy the jar file from the target directory to the 'providers' directory in your Keycloak installation
4. restart Keycloak

### Launching supporting SSI IDP

This repository also contain a simple IDP provider implementation that uses [Walt ID](https://walt.id) as a Credential Verifier.
The directory `idp` contains a docker-compose file that launches the IDP and the Keycloak server with the SSI Verifier provider installed.

1. Go to the `idp` directory
2. Run `docker-compose up -d`

## How to configure

1. Go to the Keycloak admin console
2. Go to the realm you want to configure
3. Go to the 'Identity Providers' tab
4. Click on 'Add provider' and select 'SSI Verifier'
5. Fill the `IDP Url` with the URL of the IDP you want to use (e.g. using provided idp `http://localhost:80`)
6. Fill the `Credential Type` with the Type of the credential you want to verify.
7. Fill the `Verifier Url` with the URL of the Verifier you want to use (e.g. using provided idp `http://localhost:80`)

> **NOTE:** The verifier referenced by `verifierUrl` **MUST** be the same as the one used by the IDP.  \
> In the provided example, the IDP and the Verifier use the same service, hosted on a Dizme Organization endpoint, i.e. [Walt Id Verifier](https://verifier-walt-aws.dizme.io/). 

8. Click on 'Save'
9. Go to the 'Authentication' tab
10. Go to the 'Flows' tab
11. Select the flow you want to edit or copy (e.g. 'Browser')
12. Remove every step except from `Cookie` and `Identity Provider Redirector`
12. Add the 'ssi-verifier' execution as `Identity Provider Redirector -> Default provider` to the flow
13. Click on 'Save'
14. Select Action _Bind Flow_
15. Select the flow you want to use (e.g. 'Browser')
