import * as cbor from 'cbor-web';
import { Buffer } from 'buffer';

async function decodeCBOR(vpToken: string, credentialType: string) {
    try {
        console.log("Decoding Base64 VP Token to Buffer...");
        const buf = Buffer.from(vpToken, 'base64');
        console.log("Buffer created:", buf);

        console.log("Decoding CBOR from Buffer...");
        const decoded = await cbor.decodeFirst(buf);
        console.log("Initial CBOR Decoded:", decoded);

        // Assuming the structure as given
        console.log(`Accessing issuer data for credential type: ${credentialType}`);
        const issuerDataArray = decoded.documents[0].issuerSigned.nameSpaces[credentialType];
        console.log("Issuer Data Array Retrieved:", issuerDataArray);

        console.log("Decoding nested CBOR byte strings in array...");
        const results = await Promise.all(issuerDataArray.map(async (item: any) => {
            const furtherDecodedValue = await cbor.decodeFirst(item.value);
            console.log("Further Decoded Value:", furtherDecodedValue);
            // Return the object directly as extracted from furtherDecodedValue
            return {
                elementIdentifier: furtherDecodedValue.elementIdentifier,
                elementValue: furtherDecodedValue.elementValue
            };
        }));
        console.log("All Items Decoded and Processed:", results);

        return results;
    } catch (error) {
        console.error("Error decoding CBOR:", error);
        throw error;
    }
}

export default decodeCBOR