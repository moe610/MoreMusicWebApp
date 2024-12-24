import React, { useEffect, useState, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { validateJwtToken } from './utils/authUtils.js';
import "./styles.css"
import 'bootstrap/dist/css/bootstrap.min.css';

function UploadPage(){
  const navigate = useNavigate();
  const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
  const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
  const apiUploadUrl = `${apiUrl}/api/v1/uploadAudioFiles`;
  const [token, setToken] = useState(localStorage.getItem('jwtToken'));
  const [youtubeUrl, setYoutubeUrl] = useState('');
  const [message, setMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const fetchUploadResponse = async () => {
    if (token) {
      try {
        setIsSubmitting(true); // Disable submit button during submission
        setMessage(''); // Clear previous message

        console.log(youtubeUrl);

        //Send the POST request with the youtube URL
        const response = await fetch(`${apiUploadUrl}`, {
          method: 'POST',
          headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
          },
          body: JSON.stringify({youtubeUrl})
        });
        
        if (response.ok){
          setMessage('Audio file uploaded successfully!');
        } else {
          const errorText = await response.text();
          throw new Error(errorText);
        }
      } catch (error) {
        setMessage(`Error: ${error.message}`);
      } finally {
        setIsSubmitting(false); // Re-enable the submit button
      }
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault(); // Prevent the default form submission
    fetchUploadResponse(); // Call the upload function
  };

  //Initialize webpage
  useEffect(() => {
    validateJwtToken(navigate);
  }, [navigate]);

  return (
    <div className="body-dark-gray">
      <div className="top-container">
        <Link to="/" className="home-link">Home</Link>
        <Link to="/music" className="music-link">Music</Link>
      </div>

      <h2 className="title-40">Upload Music</h2>

      <div className="upload-container">
        <div className="form-group">
          <label className="upload-label" htmlFor="youtubeUrl">YouTube URL:</label>
          <input
            type="text"
            id="youtubeUrl"
            className="form-control"
            value={youtubeUrl}
            onChange={(e) => setYoutubeUrl(e.target.value)}
          />
        </div>
        <button
          id="submitButton"
          className="upload-button btn btn-primary"
          onClick={handleSubmit}
          disabled={isSubmitting} // Disable when submitting
        >
          Submit
        </button>
        {message && (
          <div id="message" className={`alert mt-3 ${message.includes('Error') ? 'alert-danger' : 'alert-success'}`}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
}

export default UploadPage;