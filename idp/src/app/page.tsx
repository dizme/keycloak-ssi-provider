'use client';
import React, { useEffect, useState, useRef } from 'react';
import { NextPage } from 'next';
import QRCode from 'react-qr-code';
import Button from "@/components/Button";
import { v4 as uuid } from "uuid";
import { getCustomPresentation } from "@/utils/presentationDefinitions";
import decodeCBOR from "@/utils/cborDecode";

const Page: NextPage = () => {
  const [verificationState, setVerificationState] = useState<string>('');
  const [qrValue, setQrValue] = useState<string>('');
  const [credentialType, setCredentialType] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [verifierUrl, setVerifierUrl] = useState('');
  const [walletUrl, setWalletUrl] = useState('');
  const pollingIntervalRef = useRef<number | null>(null);

  useEffect(() => {
    setVerifierUrl(process.env.NEXT_PUBLIC_VERIFIER_URL || '');
    setWalletUrl(process.env.NEXT_PUBLIC_WALLET_URL || '');
    const params = new URLSearchParams(window.location.search);
    const credentialType = params.get('credentialType');
    setCredentialType(credentialType || '');
    if (credentialType) {
      getVerificationRequestUri(credentialType);
    }
    return () => {
      if (pollingIntervalRef.current) {
        clearInterval(pollingIntervalRef.current);
      }
    };
  }, []);

  const getVerificationRequestUri = async (credentialType: string) => {
    const presentationDefinition = getCustomPresentation(
        uuid(),
        credentialType,
        credentialType,
        ["age_over_18", "nationality"]
    );
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_VERIFIER_URL}/ui/presentations`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          type: "vp_token",
          response_mode: "direct_post",
          presentation_definition: presentationDefinition,
          nonce: uuid()
        })
      });
      const data = await response.json();  // Changed from .text() to .json() to parse the JSON response

      // Accessing attributes from the parsed object
      const presentationId = data.presentation_id;
      const clientId = data.client_id;
      const requestUri = data.request_uri;
      // URI Base and parameters
      const base = process.env.NEXT_PUBLIC_OIDC4VP_SCHEME;
      const encodedClientId = encodeURIComponent(clientId);
      const encodedRequestUri = encodeURIComponent(requestUri);

      // Constructing the custom URI
      const customUri = `${base}${verifierUrl}?client_id=${encodedClientId}&request_uri=${encodedRequestUri}`;

      console.debug(customUri);

      setVerificationState(presentationId);
      pollEndpoint(presentationId, credentialType);
      setQrValue(customUri);
      setLoading(false);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const pollEndpoint = (verification_state: string, credentialType: string) => {
    // Clear any existing interval to avoid duplicates
    if (pollingIntervalRef.current !== null) {
      clearInterval(pollingIntervalRef.current);
    }
    // Assign the interval and ensure the type is number
    pollingIntervalRef.current = window.setInterval(() => {
      fetch(`${process.env.NEXT_PUBLIC_VERIFIER_URL}/ui/presentations/${verification_state}`)
          .then(response => response.json())
          .then(async (data: TokenResponse) => {
            if (data.vp_token) {
              clearInterval(pollingIntervalRef.current as number);
              console.log('Token response received:', data.vp_token);
              const attributes: DecodedElement[] = await decodeCBOR(data.vp_token, credentialType)
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
      formDataParams.append(element.elementIdentifier, element.elementValue.toString())
    })
    if (redirectUri) {
      // if redirectUri contains queryParams then use &to concatenate, else use ?
      if (redirectUri.includes("?")) {
        redirectUri += "&";
      } else {
        redirectUri += "?";
      }
      redirectUri += formDataParams.toString();
      console.log(redirectUri)
      //     perform redirect
      window.location.href = redirectUri;
    }
  };

  const handleCopy = async () => {
    await navigator.clipboard.writeText(qrValue);
    alert("Verification URI copied to clipboard");
  };

  // const openWebWallet = () => {
  //   let walletUrl = qrValue.replace("openid4vp://authorize", "");
  //   walletUrl = `${process.env.NEXT_PUBLIC_WALLET_URL}/api/siop/initiatePresentation${walletUrl}`;
  //   window.open(walletUrl, "_blank");
  // };

  return (
      <div className="flex flex-col items-center justify-center bg-gray-50 min-h-screen">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-900 sm:text-3xl lg:text-4xl mt-5">
            Login with {credentialType} credential
          </h1>
          <div className="my-10 inline-block">
            {
              loading ?
                  <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-gray-900 my-10"></div>
                  :
                  <QRCode value={qrValue} viewBox="0 0 256 256"/>
            }
          </div>
          <div className="flex flex-col sm:flex-row gap-5 justify-center items-center">
            <Button onClick={handleCopy}
                    className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white">
              Copy to Clipboard
            </Button>
            {/*<Button onClick={openWebWallet}*/}
            {/*        className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white">*/}
            {/*  Open Web Wallet*/}
            {/*</Button>*/}
          </div>
        </div>
      </div>
  );
};

export default Page;