const pidAgeAndNationality = {
    "id": "32f54163-7166-48f1-93d8-ff217bdb0653", // to change at runtime
    "input_descriptors": [
        {
            "id": "eu.europa.ec.eudiw.pid.1",
            "name": "EUDI PID",
            "purpose": "We need to verify your identity",
            "format": {
                "mso_mdoc": {
                    "alg": [
                        "ES256",
                        "ES384",
                        "ES512",
                        "EdDSA",
                        "ESB256",
                        "ESB320",
                        "ESB384",
                        "ESB512"
                    ]
                }
            },
            "constraints": {
                "fields": [
                    {
                        "path": [
                            "$['eu.europa.ec.eudiw.pid.1']['age_over_18']"
                        ],
                        "intent_to_retain": false
                    },
                    {
                        "path": [
                            "$['eu.europa.ec.eudiw.pid.1']['nationality']"
                        ],
                        "intent_to_retain": false
                    }
                ]
            }
        }
    ]
}

const customPresentation = {
    "id": "32f54163-7166-48f1-93d8-ff217bdb0653", // to change at runtime
    "input_descriptors": [
        {
            "id": "PID_CREDENTIAL_NAME", // // to change at runtime
            "name": "EUDI PID",
            "purpose": "We need to verify your identity",
            "format": {
                "mso_mdoc": {
                    "alg": [
                        "ES256",
                        "ES384",
                        "ES512",
                        "EdDSA",
                        "ESB256",
                        "ESB320",
                        "ESB384",
                        "ESB512"
                    ]
                }
            },
            "constraints": {
                "fields": [
                    {
                        "path": [
                            "$['eu.europa.ec.eudiw.pid.1']['age_over_18']"
                        ],
                        "intent_to_retain": false
                    },
                    {
                        "path": [
                            "$['eu.europa.ec.eudiw.pid.1']['nationality']"
                        ],
                        "intent_to_retain": false
                    },
                    {
                        "path": [
                            "$['eu.europa.ec.eudi.pid.1']['document_number']"
                        ],
                        "intent_to_retain": false
                    }
                ]
            }
        }
    ]
}

function getCustomPresentation(id: string, credentialType: string, credentialName: string = credentialType, attributes: string[]) {
    const newPresentation = JSON.parse(JSON.stringify(customPresentation)); // Deep copy to avoid mutating the original object
    newPresentation.id = id; // Set the new ID for the presentation

    // Update the input descriptor
    newPresentation.input_descriptors[0].id = credentialType; // Change the ID of the credential
    newPresentation.input_descriptors[0].name = credentialName; // Optionally update the name to reflect the credential type

    // Update the paths based on the attributes array
    newPresentation.input_descriptors[0].constraints.fields = attributes.map(attr => ({
        path: [`$['${credentialType}']['${attr}']`],
        intent_to_retain: false
    }));

    return newPresentation;
}

export { pidAgeAndNationality, customPresentation, getCustomPresentation}
