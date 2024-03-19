// Function to parse query parameters from URL
function getQueryParams(queryString = window.location.search.substring(1)) {
    var params = {};
    var pairs = queryString.split("&");
    for (var i = 0; i < pairs.length; i++) {
        var pair = pairs[i].split("=");
        params[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1]);
    }
    return params;
}
// Function to poll the endpoint until a response containing tokenResponse is received
function pollEndpoint(verification_state) {
    // Interval ID for the polling
    var intervalId;

    // Function to make the fetch call and check for tokenResponse
    function fetchData() {
        fetch(`https://verifier-walt-aws.dizme.io/openid4vc/session/${verification_state}`)
            .then(response => response.json())
            .then(data => {
                console.log('Polling response:', data);
                if (data.tokenResponse) {
                    // Token response received, stop polling
                    clearInterval(intervalId);
                    // Perform any further actions with the token response here
                    console.log('Token response received:', data.tokenResponse);
                    performRedirect(verification_state);
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    // Start polling the endpoint every second
    intervalId = setInterval(fetchData, 1000);
}

function performRedirect(verification_state) {
    var params = getQueryParams();
    if (params["redirectUri"]) {
        var redirectId = verification_state;
        var redirectUri = params["redirectUri"];

        // Construct form data
        var formData = new FormData();
        formData.append("id", redirectId);

        // Redirect to the specified URI with form data
        if (redirectUri) {
            // if redirectUri contains queryParams then use &to concatenate, else use ?
            if (redirectUri.includes("?")) {
                redirectUri += "&";
            } else {
                redirectUri += "?";
            }
            redirectUri += new URLSearchParams(formData).toString();
            //     perform redirect
            window.location.href = redirectUri;
        }
    }
}

// Function to display headers and query parameters
function checkParams() {

    // get query parameters
    var params = getQueryParams();

    document.getElementById("credentyalType").textContent = params["credentialType"];

    // Perform POST request if credentialType is present
    if (params["credentialType"]) {
        var credentialType = params["credentialType"];
        let successRedirectUri = window.location.origin + "/callback/success";
        let errorRedirectUri = window.location.origin + "/callback/error";

        let headers = {
            'accept': '*/*',
            'Content-Type': 'application/json',
            'successRedirectUri': successRedirectUri,
            'errorRedirectUri': errorRedirectUri
        };


        fetch('https://verifier-walt-aws.dizme.io/openid4vc/verify', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                "request_credentials": [
                    credentialType
                ]
            })
        })
            .then(response => response.text())
            .then(data => {
                console.log('Success:', data);
                // data is like openid4vp://authorize?response_type=vp_token&client_id=https%3A%2F%2Fverifier-walt-aws.dizme.io%2Fopenid4vc%2Fverify&response_mode=direct_post&state=dff7716c-528e-48a8-9223-2ad889b251bb&presentation_definition_uri=https%3A%2F%2Fverifier-walt-aws.dizme.io%2Fopenid4vc%2Fpd%2Fdff7716c-528e-48a8-9223-2ad889b251bb&client_id_scheme=redirect_uri&response_uri=https%3A%2F%2Fverifier-walt-aws.dizme.io%2Fopenid4vc%2Fverify%2Fdff7716c-528e-48a8-9223-2ad889b251bb
                // I need state
                queryParamsOfData = getQueryParams(data.split("?")[1]);
                console.log(queryParamsOfData);
                var verification_state = queryParamsOfData["state"];
                // Call the function to start polling with the verification state
                pollEndpoint(verification_state);

                // Generate QR code for the response
                var qrCodeDiv = document.getElementById("qr-code");
                var qrCode = new QRCode(qrCodeDiv, {
                    text: data,
                    width: 300,
                    height: 300
                });

                // Button to copy response text
                var copyDataButton = document.getElementById("copy-button");
                copyDataButton.textContent = "Copy to clipboard";
                copyDataButton.onclick = function () {
                    navigator.clipboard.writeText(data)
                        .then(() => alert("Verification URI copied to clipboard"))
                        .catch(err => console.error('Error copying result: ', err));
                };

                // Button to open web wallet with data text
                var openWalletButton = document.getElementById("open-wallet-button");
                openWalletButton.textContent = "Open Web Wallet";
                openWalletButton.onclick = function () {
                    // remove openid4vp://authorize from data
                    data = data.replace("openid4vp://authorize", "");
                    var webWalletUrl = "https://wallet-walt-aws.dizme.io/api/siop/initiatePresentation" + data;
                    window.open(webWalletUrl, "_blank");
                };
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

// Call checkParams when the page loads
window.onload = checkParams;