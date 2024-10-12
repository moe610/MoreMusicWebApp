const currentPath = window.location.pathname;
const contextPath = currentPath.split('/')[1];
const apiUrlLogin = `/${contextPath}/login`;
const apiUrlValidate = `/${contextPath}/api/v1/auth/validate`;

async function validateJwtToken() {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        console.error('No token found, redirecting to login.');
        window.location.href = apiUrlLogin;
    } else {
        try {
            const response = await fetch(apiUrlValidate, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (response.ok) {
                console.log('Token exists and is valid:', token);
            } else {
                console.error('Token is invalid or expired, redirecting to login.');
                window.location.href = apiUrlLogin;
            }
        } catch (error) {
            console.error('Error validating token:', error);
            window.location.href = apiUrlLogin;
        }
    }
}