import React, { useEffect, useState, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { validateJwtToken } from './utils/authUtils.js';

import "./styles.css";

function MusicPage() {
  const navigate = useNavigate();
  const apiUrl = `${import.meta.env.VITE_API_URL}`;
  const apiCurrentUser = `${apiUrl}/api/v1/applicationUsers/currentUser`;
  const apiAllUsers = `${apiUrl}/api/v1/applicationUsers/userNames`;
  
  const [audioFiles, setAudioFiles] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [skipTime, setSkipTime] = useState(0);
  const [token, setToken] = useState(localStorage.getItem('jwtToken'));
  const [isPlaying, setIsPlaying] = useState(false);
  const [userId, setUserId] = useState(0);
  const [userName, setUserName] = useState("");
  const [toggled, setToggled] = useState(false);
  
  const audioRef = useRef(null);
  
  // Fetch initial data when the page loads for the logged-in user
  const fetchInitialData = async () => {
    if (token) {
      try {
        const audioFilesResponse = await fetch(`${apiUrl}/api/v1/audioFiles/files`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        const audioFilesData = await audioFilesResponse.json();
        setAudioFiles(audioFilesData);

      } catch (error) {
        console.error('Error fetching initial data:', error);
      }

      try{
        const userDataResponse = await fetch(`${apiCurrentUser}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        const userData = await userDataResponse.json();
        setUserId(userData.id);
        setUserName(userData.userName);

      } catch (error) {
        console.error('Error fetching user data:', error);
      }

      try{
        const allUserDataResponse = await fetch(`${apiAllUsers}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        const allUserData = await allUserDataResponse.json();

        setAllUsers(Array.isArray(allUserData) ? allUserData : []);

      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    } else {
      console.error("No JWT token found in localStorage");
    }
  };

  const fetchShuffleAudioFiles = async (toggled) => {
    if (token){
      try{
        const audioFileResponse = await fetch(`${apiUrl}/api/v1/audioFiles/shuffle/${userId}/${toggled}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        const audioFilesData = await audioFileResponse.json();
        setAudioFiles(audioFilesData);

      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    }
  }  

  useEffect(() => {
    // Add the onTimeUpdate event listener
    if (audioRef.current) {
      const handleTimeUpdate = () => {
        if (audioRef.current.currentTime >= skipTime) {
          playNextAudio();
        }
      };

      audioRef.current.addEventListener('timeupdate', handleTimeUpdate);

      return () => {
        // Cleanup event listener on unmount
        audioRef.current.removeEventListener('timeupdate', handleTimeUpdate);
      };
    }
  }, [skipTime, currentIndex]); // Re-run when skipTime or currentIndex changes

  useEffect(() => {
    if(audioFiles.length > 0){
      getCurrentAudioFile(audioFiles);
    }
  }, [audioFiles]);

  const getCurrentAudioFile = (files) => {
    const audioFiles = files;
    let skipTime = 0;
    let currentIndex = 0;
    setCurrentIndex(currentIndex);
  
    // Populate the scrollable list
    audioFiles.forEach((file, index) => {
        const listItem = document.createElement('div');
        listItem.className = 'audio-item';
        listItem.textContent = file.title; // Display the title
  
        listItem.onclick = () => {
            currentIndex = index; // Set the current index
            setCurrentIndex(currentIndex);
            listItem.classList.add('active'); // Highlight the selected item
            loadAudioFile(currentIndex); // Load the selected audio file
            skipTime = audioFiles[currentIndex].duration - 2;
            setSkipTime(skipTime);
        };
    });
  
    // Initialize media session metadata
    if (audioFiles.length > 0) {
        loadAudioFile(currentIndex);
    }
  };

  const loadAudioFile = (index) => {
    if (index >= 0 && index < audioFiles.length) {
      const newFile = audioFiles[index];
      audioRef.current.src = `${apiUrl}/MusicFiles/${encodeURIComponent(newFile.fileName)}`;
      document.title = newFile.title;
      audioRef.current.load();
      audioRef.current
        .play()
        .then(() => setIsPlaying(true))
        .catch(error => console.error('Error playing audio:', error));
      setSkipTime(newFile.duration - 2);
    }
  };

  const togglePlay = () => {
    if (isPlaying) {
      audioRef.current.pause();
    } else {
      audioRef.current.play().catch(error => {
        if (error.name === "NotAllowedError") {
          console.error("User interaction is required to play audio.");
          alert("Please interact with the page to start playback.");
        } else {
          console.error("Error playing audio:", error);
        }
      });
    }
    setIsPlaying(!isPlaying);
  };

  const playNextAudio = () => {
    if (currentIndex < audioFiles.length - 1) {
      const newIndex = currentIndex + 1;
      setCurrentIndex(newIndex);
      loadAudioFile(newIndex);
      setSkipTime(audioFiles[newIndex].duration - 2);

      // Play the audio immediately
      audioRef.current.play()
      .then(() => setIsPlaying(true))
      .catch(error => console.error('Error playing audio:', error));
    }
  };
  
  const playPreviousAudio = () => {
    if (currentIndex > 0) {
      const newIndex = currentIndex - 1;
      setCurrentIndex(newIndex);
      loadAudioFile(newIndex);
      setSkipTime(audioFiles[newIndex].duration - 2);

      // Play the audio immediately
      audioRef.current.play()
      .then(() => setIsPlaying(true))
      .catch(error => console.error('Error playing audio:', error));
    }
  };

  useEffect(() => {
    if (audioFiles.length > 0) {
      loadAudioFile(currentIndex);
    }
  }, [audioFiles]);

  useEffect(() => {
    validateJwtToken(navigate);
    fetchInitialData();
  }, [navigate]);

  return (
    <div className="body-dark-gray">
      <div className="top-container">
        <Link to="/" className="home-link">Home</Link>
        <Link to="/upload" className="upload-link">Upload</Link>
      </div>

      <h2 className="title-40">Music</h2>

      <div className="audio-container">
        <audio ref={audioRef} controls></audio>
      </div>

      <div className="select-container">
        <div className="toggle-container">
          <h2 className="title-40">Shuffle</h2>
          <button
            className={`toggle-btn ${toggled ? "toggled" : ""}`}
            onClick={() => {
                setToggled(!toggled);
                fetchShuffleAudioFiles(!toggled);
              }
            }
          >
            <div className="thumb"></div>
          </button>
        </div>
        <div className="dropdown-container">
          <div className="dropdown">
            <select>
              onChange={(e) =>{
                const c = allUsers?.find((x) => x.id === e.target.value)
                console.log(c)
              }}
              {allUsers
                ? allUsers.map((user) => {
                  return <option key={user.id} value={user.username}>{user.username}</option>
                })
                : null
              }
            </select>
          </div>
        </div>
      </div>
      
      <div className="button-container">
        <button className="button-40" role="button" onClick={playPreviousAudio}>Previous</button>
        <button className="button-40" role="button" onClick={togglePlay}>Play/Pause</button>
        <button className="button-40" role="button" onClick={playNextAudio}>Next</button>
      </div>

      <div className="audio-list" id="audio-list">
        {audioFiles.map((file, index) => (
          <div 
            key={file.id} 
            className={`audio-item ${index === currentIndex ? 'active' : ''}`} 
            onClick={() => loadAudioFile(index)}
          >
            {file.title}
          </div>
        ))}
      </div>
    </div>
  );
}

export default MusicPage;
