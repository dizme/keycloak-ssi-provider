type TokenResponse = {
    vp_token: string;
    presentation_submission: {
        id: string;
        definition_id: string;
        descriptor_map: Array<{
            id: string;
            format: string;
            path: string;
        }>;
    };
};

interface DecodedElement {
    elementIdentifier: string;
    elementValue: any;
}