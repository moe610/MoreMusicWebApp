import { useState } from "react";
import "./styles.css"
import "./loginstyle.css"
import { Link, useNavigate } from 'react-router-dom';


function Register(){
  // React state for form inputs and error handling
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate(); // For redirecting after successful login

  const handleSubmit = async (event) => {
    event.preventDefault(); // Prevent form from submitting in the default way

    // Get the API URL from environment variable
    const apiUrlRegister = `${import.meta.env.VITE_API_URL}/api/v1/auth/register`;
    const urlHome = `${import.meta.env.VITE_BASE_URL}`;

    const credentials = { firstName, lastName, email, userName, password };

    try {
      const response = await fetch(apiUrlRegister, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        throw new Error("Failed to register user.");
      }

      const data = await response.json();
      const token = data.token;

      if (token) {
        localStorage.setItem("jwtToken", token);
        navigate(urlHome); // Redirect to home after successful login
      } else {
        throw new Error("Token validation failed. Please try again.");
      }
    } catch (error) {
      console.error("Registration error:", error);
      setErrorMessage("Registration failed: " + error.message); // Set error message to state
    }
  };

  return (
    <div className="body-dark-gray login-register-body">
        <div className="login-container">
            <h2>Register</h2>
            {/* Conditionally render error message */}
            {errorMessage && <div id="error-message" className="error-message">{errorMessage}</div>}

            <form id="registerForm" onSubmit={handleSubmit}>
                <input 
                  className="input_text" 
                  type="text" 
                  id="firstName"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)} 
                  placeholder="Firstname" 
                  required 
                />
                <input 
                  className="input_text" 
                  type="text" 
                  id="lastName"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  placeholder="Lastname" 
                  required 
                />
                <input 
                  className="input_text" 
                  type="text" 
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Email" 
                  required 
                />
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
                <button type="submit">Register</button>
            </form>

            <div className="register-link">
                <p style={{ color: "white" }}>Have an account? <Link to="/login">Login here</Link> </p>
            </div>
        </div>
    </div>
  )
}

export default Register;