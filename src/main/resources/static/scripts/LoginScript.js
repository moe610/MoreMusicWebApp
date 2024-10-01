document.getElementById("loginForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const currentPath = window.location.pathname;
    const contextPath = currentPath.split('/')[1];
    const apiUrl = `/${contextPath}/api/v1/auth/authenticate`;
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
            throw new Error("Failed to authenticate");
        }

        const data = await response.json();
        const token = data.token; // Assuming your response JSON contains a 'token' field

        // Store the token in localStorage
        localStorage.setItem("jwtToken", token);

        alert("Login successful! Token stored in localStorage.");
        window.location.href = "/dashboard"; // Redirect to the dashboard or some other page
    } catch (error) {
        document.getElementById("error-message").textContent = "Login failed: " + error.message;
        document.getElementById("error-message").style.display = "block";
    }
});