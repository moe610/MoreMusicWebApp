document.getElementById("registerForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const currentPath = window.location.pathname;
    const contextPath = currentPath.split('/')[1];
    const apiUrl = `/${contextPath}/api/v1/auth/register`;
    const apiUrlLogin = `/${contextPath}/login`;

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const userName = document.getElementById("userName").value;
    const password = document.getElementById("password").value;

    const credentials = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        userName: userName,
        password: password
    };

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(credentials)
        });

        if (!response.ok) {
            throw new Error("Failed to register.");
        }

        const data = await response.json();
        const token = data.token;

        localStorage.setItem("jwtToken", token);

        if (token != null) {
            window.location.href = apiUrlLogin;
        } else {
            throw new Error('Token validation failed. Please log in again.');
        }

    } catch (error) {
        console.error('Login error:', error);
        document.getElementById("error-message").textContent = "Registration failed: " + error.message;
        document.getElementById("error-message").style.display = "block";
    }
});
