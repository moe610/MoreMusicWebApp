import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { validateJwtToken } from './utils/authUtils.js';
import "./styles.css"
import { Link } from 'react-router-dom';

function Home(){
  const navigate = useNavigate();

  useEffect(() => {
        validateJwtToken(navigate);
  }, [navigate]);

  return (
    <div className="body-dark-gray">
        <h1 className="title-40">More Music Web Application</h1>

        <div className="home-button-container">
            <Link className="button-40" to="/music">Listen</Link>
            <Link className="button-40" to="/upload">Upload</Link>
        </div>
    </div>
  )
}

export default Home;