import { useState } from "react";
import "./styles.css"
import "./loginstyle.css"
import { Link, useNavigate, useLocation } from 'react-router-dom';

function ResetPassword(){
  //React state for form inputs and error handling
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const resetToken = params.get("token");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState('');
  const navigate = useNavigate(); // For redirecting after successful login

  const handleSubmit = async (event) => {
    event.preventDefault(); // Prevent form from submitting in the default way

    // Get the API URL from environment variable
    const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
    const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
    const apiUrlResetPassword = `${apiUrl}/api/v1/auth/resetPassword`;
    const credentials = { password, confirmPassword, resetToken };

    try {
      const response = await fetch(apiUrlResetPassword, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      const resetResponse = await response.text();

      if (response.ok){
        setMessage(resetResponse);
      } else {
        throw new Error(resetResponse);
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
                  type="password"
                  id="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Password"  
                />
                <input 
                  className="input_text" 
                  type="password" 
                  id="confirmPassword" 
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Confirm Password" 
                />
                <button type="submit">Reset Password</button>
            </form>

            <div className="register-link">
                <p style={{ color: "white" }}>Return to login <Link to="/login">Login Here</Link> </p>
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

export default ResetPassword;