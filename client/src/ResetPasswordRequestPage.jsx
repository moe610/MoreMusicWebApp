import { useState } from "react";
import "./styles.css"
import "./loginstyle.css"
import { Link, useNavigate } from 'react-router-dom';

function ResetPasswordRequest(){
  // React state for form inputs and error handling
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState('');
  const navigate = useNavigate(); // For redirecting after successful login

  const handleSubmit = async (event) => {
    event.preventDefault(); // Prevent form from submitting in the default way

    // Get the API URL from environment variable
    const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
    const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
    const apiUrlResetPasswordRequest = `${apiUrl}/api/v1/applicationUsers/resetPasswordRequest`;
    const credentials = { userName, email };

    try {
      const response = await fetch(apiUrlResetPasswordRequest, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      const resetRequestReponse = await response.text();

      if (response.ok){
        setMessage(resetRequestReponse);
      } else {
        throw new Error(resetRequestReponse);
      }
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    }
  };

  return (
    <div className="body-dark-gray login-register-body">
        <div className="login-container">
            <h2>Password Reset</h2>

            <form id="restPasswordForm"  onSubmit={handleSubmit}>
                <input 
                  className="input_text" 
                  type="text" 
                  id="userName" 
                  value={userName}
                  onChange={(e) => setUserName(e.target.value)}
                  placeholder="Username"  
                />
                <p className="reset-Pass-Paragraph">Or</p>
                <input 
                  className="input_text" 
                  type="text" 
                  id="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Email Address" 
                />
                <button type="submit">Reset Password</button>
            </form>

            <div className="register-link">
                <p style={{ color: "white" }}>Return to login <Link to="/login">Login here</Link> </p>
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

export default ResetPasswordRequest;