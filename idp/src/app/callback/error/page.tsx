import React from 'react';

const ErrorPage = () => {
    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="container">
                <div className="bg-white rounded-lg shadow px-5 py-10 text-center">
                    <h1 className="text-4xl font-bold text-red-600">Error</h1>
                    <p className="mt-4 text-lg text-gray-700">There was an error. Please try again later.</p>
                </div>
            </div>
        </div>
    );
};

export default ErrorPage;