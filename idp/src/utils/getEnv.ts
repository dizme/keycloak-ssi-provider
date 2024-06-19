export type EnvSettings = {
    VERIFIER_URL: string;
    OIDC4VP_SCHEME: string;
    PRESENTATION_FORMAT: string;
};

export default function getEnv(): EnvSettings {
    const parsedSettings: Partial<EnvSettings> = {
        VERIFIER_URL: process.env.VERIFIER_URL,
        OIDC4VP_SCHEME: process.env.OIDC4VP_SCHEME,
        PRESENTATION_FORMAT: process.env.PRESENTATION_FORMAT,
    };

    // Ensure all required settings are available
    const requiredSettings: (keyof EnvSettings)[] = [
        'VERIFIER_URL',
        'OIDC4VP_SCHEME',
    ];

    for (const key of requiredSettings) {
        if (!parsedSettings[key]) {
            throw new Error(`Missing setting: ${key}`);
        }
    }

    return parsedSettings as EnvSettings;
}
