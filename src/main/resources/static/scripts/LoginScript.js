document.getElementById("loginForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const currentPath = window.location.pathname;
    const contextPath = currentPath.split('/')[1];
    const apiUrl = `/${contextPath}/api/v1/auth/authenticate`;
    const apiUrlHome = `/${contextPath}/`;

    const userName = document.getElementById("userName").value;
    const password = document.getElementById("password").value;

    const credentials = {
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
            throw new Error("Failed to authenticate. Please check your username and password.");
        }

        const data = await response.json();
        const token = data.token;

        localStorage.setItem("jwtToken", token);

        if (token != null) {
            window.location.href = apiUrlHome;
        } else {
            throw new Error('Token validation failed. Please log in again.');
        }

    } catch (error) {
        console.error('Login error:', error);
        document.getElementById("error-message").textContent = "Login failed: " + error.message;
        document.getElementById("error-message").style.display = "block";
    }
});
