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
  const [message, setMessage] = useState('');
  const navigate = useNavigate(); // For redirecting after successful login

  const handleSubmit = async (event) => {
    event.preventDefault(); // Prevent form from submitting in the default way

    // Get the API URL from environment variable
    const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
    const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
    const apiUrlRegister = `${apiUrl}/api/v1/auth/register`;
    const credentials = { firstName, lastName, email, userName, password };

    try {
      const response = await fetch(apiUrlRegister, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      const registerResponse = await response.text();

      if (response.ok){
        setMessage(registerResponse);
      } else {
        throw new Error(registerResponse);
      }
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    }
  };

  return (
    <div className="body-dark-gray login-register-body">
        <div className="login-container">
            <h2>Register</h2>

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

export default Register;