import "./styles.css"
import { Link } from 'react-router-dom';

function UploadPage(){
  const [youtubeUrl, setYoutubeUrl] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = () => {
     //Placeholder for submit logic
    setMessage('URL submitted successfully!');
  };

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
          className="btn btn-primary"
          onClick={handleSubmit}
        >
          Submit
        </button>
        {message && (
          <div id="message" className="alert mt-3">
            {message}
          </div>
        )}
      </div>
    </div>
  );
}

export default UploadPage;