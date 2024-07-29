// Assuming the typescript file is using .tsx extension and located appropriately
'use client';

import React, { useState } from 'react';
import decodeCBOR from "@/utils/cborDecode";

// Define the structure of the data you expect to receive
interface DecodedElement {
    elementIdentifier: string;
    elementValue: any;
}

const TestPage = () => {
    const [vpToken, setVpToken] = useState<string>(
        'o2d2ZXJzaW9uYzEuMGlkb2N1bWVudHOBo2dkb2NUeXBleBhldS5ldXJvcGEuZWMuZXVkaXcucGlkLjFsaXNzdWVyU2lnbmVkompuYW1lU3BhY2VzoXgYZXUuZXVyb3BhLmVjLmV1ZGl3LnBpZC4xgtgYWIGkZnJhbmRvbVhAkS4m2eZhz4-32KGuBtupMqAZ-FwQV4O2WOHOAiT2EULg0uCCUMhB7hDC9ZM2p9E1QvsCbgD6jBh33v5m4QUlp2hkaWdlc3RJRBgobGVsZW1lbnRWYWx1ZfVxZWxlbWVudElkZW50aWZpZXJrYWdlX292ZXJfMTjYGFiCpGZyYW5kb21YQIW24eR6o1hmr6_8lIAuNO0CQjyv9SI5229PUPTXQUKhSvP0utZF1du2rRQiQh9TVhzavLX6maeh6YIPV97ghYxoZGlnZXN0SUQJbGVsZW1lbnRWYWx1ZWJTRXFlbGVtZW50SWRlbnRpZmllcmtuYXRpb25hbGl0eWppc3N1ZXJBdXRohEOhASahGCFZAoUwggKBMIICJqADAgECAgkWSuWZAtwFEGQwCgYIKoZIzj0EAwIwWDELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0V1cm9wZWFuIENvbW1pc3Npb24xKzApBgNVBAMTIkVVIERpZ2l0YWwgSWRlbnRpdHkgV2FsbGV0IFRlc3QgQ0EwHhcNMjMwNTMwMTIzMDAwWhcNMjQwNTI5MTIzMDAwWjBlMQswCQYDVQQGEwJCRTEcMBoGA1UEChMTRXVyb3BlYW4gQ29tbWlzc2lvbjE4MDYGA1UEAxMvRVUgRGlnaXRhbCBJZGVudGl0eSBXYWxsZXQgVGVzdCBEb2N1bWVudCBTaWduZXIwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAR8kxP0waSqTrCz62gRpJlOWd5nmWQxwvOuCI63oQYctli9jDkSbBlZeskN-Z0HjT7zkTujS9ssvGmH0Cfpr538o4HLMIHIMB0GA1UdDgQWBBTRpLEkOTL7RXJymUjyUn2VWKdNLTAfBgNVHSMEGDAWgBQykesOHAEdFA52T2xP6kyWONr7BDAOBgNVHQ8BAf8EBAMCB4AwEgYDVR0lBAswCQYHKIGMXQUBAjAfBgNVHRIEGDAWhhRodHRwOi8vd3d3LmV1ZGl3LmRldjBBBgNVHR8EOjA4MDagNKAyhjBodHRwczovL3N0YXRpYy5ldWRpdy5kZXYvcGtpL2NybC9pc28xODAxMy1kcy5jcmwwCgYIKoZIzj0EAwIDSQAwRgIhAN5fmOce9ldSEmvyxLhP3t-B0kPKV7Fb0xiqufHr6z99AiEA_iL3MmtLV1j_Fv6G0zqNjSmIIWnaBJtaXiyAarFHCEhZBeHYGFkF3KZndmVyc2lvbmMxLjBvZGlnZXN0QWxnb3JpdGhtZ1NIQS0yNTZnZG9jVHlwZXgYZXUuZXVyb3BhLmVjLmV1ZGl3LnBpZC4xbHZhbHVlRGlnZXN0c6F4GGV1LmV1cm9wYS5lYy5ldWRpdy5waWQuMbghGCxYIFdjjZJ8R8DeBaFXRHfBo1zOB2luBNsD7e970AiwqUwTGDRYIFBZhSOZx0Bqfr2QUHE2akmGC2LZkdRRhxeis-29lu_dA1ggI_wV62n4UDkgmx7V86Iao7FQLQDscdaGQ9DUPHt_w8UYKFggKkQ8BwllRDC24SgqNXLy9ic2k4NjeqUuiKZtxmXkfbcYIFggX3zzr6v1sY43t_CEfeXdvukR_7YzE5Fa1j3Vt9z9so0YaFggFt8F0wttSTZ_XiVLu1bsV4rdViu7f2qNAO_Dcd4tDZ8YLVgg4bI5x7NU39v6Wy2IqbeHHEY11OEF9-JtKzxqrlVwppEYZ1ggq5EWtq80200WvZxk_vwiILZMhotHmKNqj43ogtu3UHkYNVgg2lkEwsCi925eEFJZjSdn4dRTUTw7RVjGQdkQKJlVgHQKWCBa2HkeKBDNzV0KWlUsweZRfJynhmY2ce5aARZ9bpRLixgiWCAlBGpvdg4XuZ7X8s_4JGp_VZ3wo3-bT8aAKp3O4RjJoRgyWCCTI2iuUqkG6QIZi-WC0xVImnU_Lfc2gIvQNveOrpInJwhYIOF_Hf4FJhDVhQYYDXqKfTLj6hD5vtR2TlfwhyZgNNPIGC9YILD8y-50rTuEF7FQQGaDw9U91yFsHOwwLBS8ADCOvL3bBVggPOlTi0kRu30wQxw7h2tyvWrB7fnOZNLaCT5-XQ9P-AAYKVggS2h6WpHn8MO-8uMBZ8Fiif1fTYlFKAYqxDkNGZ3aBx4YH1ggG05KAmTrcmm9mIYAXK1NjbVZFl3uzrpAOAgfwaF7ZJQYIVgghwbsSvM6KsJ8mWMFEazyQf5LuLpSSkSRcOHkPCEtv7MYI1ggIX1ElYJQLbSK_VPnakVUcpKbzoJpvs9ggt55fHy47HkYLlggAU63OlONKr2hS_M2SnwVeZw5t9r9cIfxJLXmXw6otm4YHlggoOSU8XFZLvMKSu_eaj1H1bHPJ5iSpfGqbY2clufEEzECWCB9ynzS2ivf4JtrZGq1XythXfVF9Hch-B9RKH12T_zXuRgkWCCSqRIRp4lZsXj3u9vuxHtyJgiracJAEmixia-9wyVzpBgwWCBrYcVnrdVnE37X4CB09K4cL5ZsaYSIpHnNgDEDue40nxgnWCCgb0JUaLvVmok2Su8UGzY6bUWTy5t8TD5t3Pr1H-V8UglYII2JKgf5FOExd2c5sBe1K9gtXGyB8irAoFmWu7QbLvetGBxYILDGAcMg8CMdvNd6QIul2MULKhEvznUXWhvdw0QYaXfwDFgg8VMf0YGnu6RgIe8DkR0vzHIaVE-2sKRkqOW5sScCP9MHWCAurJbKiBkk8GgVr-9xLTDY0RBDN3Lb2Yg3kyAzxKfJgAZYIGvQmpOLFJPm1tpheae8o72Oqenw15y1XPGvnasuS71SGCtYIA-nT-P8uWPW6h0EvvFFceTmTcGoCphju1klbcxuan24GCVYIIvrRrvd2ExiHentW6OEpLbRccigkCMnqA4W57TKfrgdGBpYIM-zM1hiC5Evo7JSYyKfH7MTcNkTYBy8sbjxL9GmcFaybWRldmljZUtleUluZm-haWRldmljZUtleaQBAiABIVggUQKmcRVrDHkabOBrCpaLb2uc79gBrJV7sSZlW6N1Uo0iWCDfCGxUI-8LDc-8VWKA6sAqokBvMSpGUaW1R2eRnuGHn2x2YWxpZGl0eUluZm-jZnNpZ25lZMB0MjAyNC0wNC0zMFQxNToxNTowMlppdmFsaWRGcm9twHQyMDI0LTA0LTMwVDE1OjE1OjAyWmp2YWxpZFVudGlswHQyMDI1LTA0LTMwVDE1OjE1OjAyWlhAzow0Ksq7lK_WyInUSaaJ3cpF3l-9Nyns7Yt5mHgOgP4upGbbuvM1fFhwSI8cTF5vzGiHIs2P4KeFtLfjwdnpPGxkZXZpY2VTaWduZWSiam5hbWVTcGFjZXPYGEGgamRldmljZUF1dGihb2RldmljZVNpZ25hdHVyZYRDoQEmoPZYQG3uTNWdGtIm5eBHxJSjmAt9PT0MDlESIcCF3QHL5m5pn1dMbE4XHkob1QzQRjGgjSnrjFLtq3CdoiT4-Oq4qcpmc3RhdHVzAA'
    );
    const [credentialType, setCredentialType] = useState<string>('eu.europa.ec.eudiw.pid.1');
    const [result, setResult] = useState<DecodedElement[]>([]); // Use the DecodedElement interface
    const [error, setError] = useState<string>('');

    const handleDecode = async () => {
        try {
            const decodedData: DecodedElement[] = await decodeCBOR(vpToken, credentialType);
            setResult(decodedData);
            setError('');
        } catch (err) {
            const error = err as Error; // Better error handling with TypeScript
            setError(error.message || 'Failed to decode data');
            console.error(err);
        }
    };

    return (
        <div>
            <h1>CBOR Decode Test</h1>
            <div>
                <label>
                    VP Token:
                    <input
                        type="text"
                        value={vpToken}
                        onChange={(e) => setVpToken(e.target.value)}
                    />
                </label>
            </div>
            <div>
                <label>
                    Credential Type:
                    <input
                        type="text"
                        value={credentialType}
                        onChange={(e) => setCredentialType(e.target.value)}
                    />
                </label>
            </div>
            <button onClick={handleDecode}>Decode</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div>
                <h2>Results:</h2>
                <pre>{JSON.stringify(result, null, 2)}</pre>
            </div>
        </div>
    );
};

export default TestPage;
