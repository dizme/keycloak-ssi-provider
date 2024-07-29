'use server';

import getEnv from '@/utils/getEnv';

export async function getSettings() {
    return getEnv();
}
