'use server';

import { v4 as uuid } from 'uuid';
import { getCustomPresentation } from '@/utils/presentationDefinitions';
import getEnv from '@/utils/getEnv';

export async function getVerificationRequestUri(credentialType: string, attributes: string[]) {
    const { VERIFIER_URL, OIDC4VP_SCHEME } = getEnv();

    const presentationDefinition = getCustomPresentation(
        uuid(),
        credentialType,
        credentialType,
        attributes
    );

    try {
        const response = await fetch(`${VERIFIER_URL}/ui/presentations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                type: "vp_token",
                presentation_definition: presentationDefinition,
                nonce: uuid()
            })
        });

        const data = await response.json();

        const presentationId = data.presentation_id;
        const clientId = data.client_id;
        const requestUri = data.request_uri;
        const encodedClientId = encodeURIComponent(clientId);
        const encodedRequestUri = encodeURIComponent(requestUri);
        // Extract the host from VERIFIER_URL
        const verifierUrlHost = new URL(VERIFIER_URL).host;
        const customUri = `${OIDC4VP_SCHEME}${verifierUrlHost}?client_id=${encodedClientId}&request_uri=${encodedRequestUri}`;

        return {
            presentationId,
            customUri,
        };
    } catch (error) {
        console.error('Error:', error);
        throw new Error('Failed to get verification request URI');
    }
}
