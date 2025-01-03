import { useState } from "react";
import "./styles.css"
import "./loginstyle.css"
import { Link, useNavigate } from 'react-router-dom';

function Login(){
  // React state for form inputs and error handling
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState('');
  const navigate = useNavigate(); // For redirecting after successful login

  const handleSubmit = async (event) => {
    event.preventDefault(); // Prevent form from submitting in the default way

    // Get the API URL from environment variable
    const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
    const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
    const apiUrlAuthenticate = `${apiUrl}/api/v1/auth/authenticate`;
    const credentials = { userName, password };

    try {
      const response = await fetch(apiUrlAuthenticate, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        const data = await response.json();
        const error = data.error;
        console.log(error);
        throw new Error("Please check your username and password.");
      }

      const data = await response.json();
      const token = data.token;

      if (token) {
        localStorage.setItem("jwtToken", token);
        navigate("/"); // Redirect to home after successful login
      } else {
        throw new Error("Token validation failed. Please log in again.");
      }
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    }
  };

  return (
    <div className="body-dark-gray login-register-body">
        <div className="login-container">
            <h2>Login</h2>

            <form id="loginForm"  onSubmit={handleSubmit}>
                <input 
                  className="input_text" 
                  type="text" 
                  id="userName" 
                  value={userName}
                  onChange={(e) => setUserName(e.target.value)}
                  placeholder="Username" 
                  required 
                />
                <input 
                  className="input_text" 
                  type="password" 
                  id="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Password" 
                  required 
                />
                <button type="submit">Login</button>
            </form>

            <div className="register-link">
                <p style={{ color: "white" }}>
                  Don't have an account? <Link to="/register">Register here</Link> 
                </p>
            </div>

            <div className="reset-link">
                <p style={{ color: "white" }}>
                  Forgot password? <Link to="/resetPasswordRequest">Reset Password</Link> 
                </p>
            </div>

            <div>
              {message && (
                <div id="message" className={`alert mt-3 ${message.includes('Error') ? 'alert-danger' : 'alert-success'}`}>
                  {message}
                </div>
              )}
            </div>
        </div>
    </div>
  )
}

export default Login;