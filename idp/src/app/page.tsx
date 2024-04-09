'use client';
import React, { useEffect, useState } from 'react';
import { NextPage } from 'next';
import QRCode from 'react-qr-code';
import Button from "@/components/Button";

type TokenResponse = {
  tokenResponse: string;
};

const Page: NextPage = () => {
  const [verificationState, setVerificationState] = useState<string>('');
  const [qrValue, setQrValue] = useState<string>('');
  const [credentialType, setCredentialType] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [verifierUrl, setVerifierUrl] = useState('');
  const [walletUrl, setWalletUrl] = useState('');

  useEffect(() => {
    setVerifierUrl(process.env.NEXT_PUBLIC_VERIFIER_URL || '');
    setWalletUrl(process.env.NEXT_PUBLIC_WALLET_URL || '');
    const params = new URLSearchParams(window.location.search);
    const credentialType = params.get('credentialType');
    setCredentialType(credentialType || '');
    if (credentialType) {
      fetchCredentials(credentialType);
    }
  }, []);

  const fetchCredentials = async (credentialType: string) => {
    const successRedirectUri = `${window.location.origin}/callback/success`;
    const errorRedirectUri = `${window.location.origin}/callback/error`;

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_VERIFIER_URL}/openid4vc/verify`, {
        method: 'POST',
        headers: {
          'accept': '*/*',
          'Content-Type': 'application/json',
          'successRedirectUri': successRedirectUri,
          'errorRedirectUri': errorRedirectUri
        },
        body: JSON.stringify({
          "request_credentials": [credentialType]
        })
      });
      const data = await response.text();
      const queryParamsOfData = new URLSearchParams(data.split("?")[1]);
      const verificationState = queryParamsOfData.get('state') || '';
      setVerificationState(verificationState);
      pollEndpoint(verificationState);
      setQrValue(data);
      setLoading(false);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const pollEndpoint = (verification_state: string) => {
    const intervalId = setInterval(() => {
      fetch(`${process.env.NEXT_PUBLIC_VERIFIER_URL}/openid4vc/session/${verification_state}`)
          .then(response => response.json())
          .then((data: TokenResponse) => {
            if (data.tokenResponse) {
              clearInterval(intervalId);
              console.log('Token response received:', data.tokenResponse);
              performRedirect(verification_state);
            }
          })
          .catch(error => {
            console.error('Error polling data:', error);
          });
    }, 1000);
  };

  const performRedirect = (verification_state: string) => {
    const params = new URLSearchParams(window.location.search);
    let redirectUri = params.get("redirectUri");
    var formData = new FormData();
    formData.append("id", verification_state);
    const formDataParams = new URLSearchParams();
    formData.forEach((value, key) => {
      formDataParams.append(key, value.toString());
    });
    if (redirectUri) {
      // if redirectUri contains queryParams then use &to concatenate, else use ?
      if (redirectUri.includes("?")) {
        redirectUri += "&";
      } else {
        redirectUri += "?";
      }
      redirectUri += formDataParams.toString();
      //     perform redirect
      window.location.href = redirectUri;
    }
  };

  const handleCopy = async () => {
    await navigator.clipboard.writeText(qrValue);
    alert("Verification URI copied to clipboard");
  };

  const openWebWallet = () => {
    let walletUrl = qrValue.replace("openid4vp://authorize", "");
    walletUrl = `${process.env.NEXT_PUBLIC_WALLET_URL}/api/siop/initiatePresentation${walletUrl}`;
    window.open(walletUrl, "_blank");
  };

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
            <Button onClick={openWebWallet}
                    className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white">
              Open Web Wallet
            </Button>
          </div>
        </div>
      </div>
  );
};

export default Page;