'use client'
import React from 'react';
import { usePathname, useSearchParams, useRouter } from 'next/navigation';
import Button from "@/components/Button";

const VerificationStatePage: React.FC = () => {
    const router = useRouter();
    const pathname = usePathname();
    const searchParams = useSearchParams();

    const verificationState = pathname.split('/').pop();
    const attributes = Object.fromEntries(searchParams.entries());

    const handleBack = () => {
        router.push('/');
    };

    return (
        <div className="flex flex-col items-center justify-center bg-gray-50 min-h-screen p-4">
            <div className="bg-white shadow-md rounded-lg p-6 max-w-4xl w-full">
                <h1 className="text-2xl font-bold text-gray-900 sm:text-3xl lg:text-4xl mb-5 break-words">
                    Verification Results
                </h1>
                <div className="my-6">
                    <h2 className="text-xl font-semibold text-gray-800 mb-3">Attributes</h2>
                    <ul className="list-disc list-inside space-y-2">
                        {Object.entries(attributes).map(([key, value]) => (
                            <li key={key} className="text-lg text-gray-700">
                                <strong>{key}:</strong> <span className="break-words">{value}</span>
                            </li>
                        ))}
                    </ul>
                </div>
                <div className="mt-6 flex justify-center">
                    <Button
                        onClick={handleBack}
                        className="bg-sky-500 hover:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white"
                    >
                        Back to Home
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default VerificationStatePage;
