'use client';

import React, { useEffect, useState, useRef } from 'react';
import { NextPage } from 'next';
import QRCode from 'react-qr-code';
import Button from "@/components/Button";
import { getSettings } from "@/app/actions/getSettings";
import { EnvSettings } from "@/utils/getEnv";
import {getVerificationRequestUri} from "@/app/actions/getVerificationRequestUri";
import decodeCBOR from "@/utils/cborDecode";

const Page: NextPage = () => {
  const [verificationState, setVerificationState] = useState<string>('');
  const [qrValue, setQrValue] = useState<string>('');
  const [credentialType, setCredentialType] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const pollingIntervalRef = useRef<number | null>(null);
  const [settings, setSettings] = useState<EnvSettings | null>(null);
  const [claims, setClaims] = useState<string[]>(["document_number"]);

  useEffect(() => {
    async function fetchSettings() {
      try {
        const settings = await getSettings();
        setSettings(settings);
      } catch (error) {
        console.error('Error fetching settings:', error);
      }
    }

    fetchSettings();
  }, []);

  useEffect(() => {
    async function fetchVerificationUri() {
      if (!settings) return;

      const verifierUrl = settings.VERIFIER_URL;
      const params = new URLSearchParams(window.location.search);
      const credentialType = params.get('credentialType');
      const claims = params.get('claims');

      setCredentialType(credentialType || '');

      if (credentialType) {
        try {
          const attributes = claims?.split(',').filter(attr => attr !== "") || [];
          setClaims(attributes);
          const { presentationId, customUri } = await getVerificationRequestUri(credentialType, attributes);
          setVerificationState(presentationId);
          setQrValue(customUri);
          pollEndpoint(presentationId, credentialType);
          setLoading(false);
        } catch (error) {
          console.error('Error fetching verification request URI:', error);
        }
      }
    }

    fetchVerificationUri();

    return () => {
      if (pollingIntervalRef.current) {
        clearInterval(pollingIntervalRef.current);
      }
    };
  }, [settings]);

  const pollEndpoint = (verification_state: string, credentialType: string) => {
    if (pollingIntervalRef.current !== null) {
      clearInterval(pollingIntervalRef.current);
    }

    pollingIntervalRef.current = window.setInterval(() => {
      fetch(`${settings?.VERIFIER_URL}/ui/presentations/${verification_state}`)
          .then(response => response.json())
          .then(async (data: TokenResponse) => {
            if (data.vp_token) {
              clearInterval(pollingIntervalRef.current as number);
              const attributes: DecodedElement[] = await decodeCBOR(data.vp_token, credentialType);
              performRedirect(verification_state, attributes);
            }
          })
          .catch(error => {
            console.error('Error polling data:', error);
          });
    }, 2000);
  };

  const performRedirect = (verification_state: string, attributes: DecodedElement[]) => {
    const params = new URLSearchParams(window.location.search);
    let redirectUri = params.get("redirectUri");

    const formDataParams = new URLSearchParams();
    formDataParams.append("id", verification_state);
    attributes.forEach((element: DecodedElement) => {
      formDataParams.append(element.elementIdentifier, element.elementValue.toString());
    });

    if (redirectUri) {
      if (redirectUri.includes("?")) {
        redirectUri += "&";
      } else {
        redirectUri += "?";
      }
      redirectUri += formDataParams.toString();
      window.location.href = redirectUri;
    } else {
      // Redirect to the recap page
      const recapUri = `/${verification_state}?${formDataParams.toString()}`;
      window.location.href = recapUri;
    }
  };


  const handleCopy = async () => {
    await navigator.clipboard.writeText(qrValue);
    alert("Verification URI copied to clipboard");
  };

  const openWallet = () => {
    window.open(qrValue, "_blank");
  };

  return (
      <div className="flex flex-col items-center justify-center bg-gray-50 min-h-screen">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-900 sm:text-3xl lg:text-4xl mt-5">
            You are requesting to present
          </h1>
          <h2 className="text-xl font-bold text-gray-400 sm:text-3xl lg:text-4xl mt-5">
            <code>{credentialType}</code>
          </h2>
          <div className="text-md font-bold text-gray-900 sm:text-lg lg:text-xl mt-5">
            Requested Claims:
            <ul>
              {claims.map((claim, index) => (
                  <li key={index} className="font-mono text-gray-500">
                    {claim}
                  </li>
              ))}
            </ul>
          </div>
          <div className="my-10 inline-block">
            {loading ? (
                <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-gray-900 my-10"></div>
            ) : (
                <QRCode value={qrValue} viewBox="0 0 256 256"/>
            )}
          </div>
          <div className="flex flex-col sm:flex-row gap-5 justify-center items-center">
            <Button
                onClick={handleCopy}
                className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white"
            >
              Copy to Clipboard
            </Button>
            <Button
                onClick={openWallet}
                className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white"
            >
              Open Mobile Wallet
            </Button>
          </div>
        </div>
      </div>
  );
};

export default Page;
