import { Routes, Route, BrowserRouter as Router } from 'react-router-dom';
import Home from './Home';
import Music from './MusicPage';
import Upload from './UploadPage';
import Login from './LoginPage';
import Register from './RegisterPage';
import ResetPasswordRequest from './ResetPasswordRequestPage';
import ResetPassword from './ResetPasswordPage';

function App() {
  return (
    <Router basename="/MoreMusicWebApp">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/resetPasswordRequest" element={<ResetPasswordRequest />} />
        <Route path="/resetPassword" element={<ResetPassword />} />
        <Route path="/music" element={<Music />} />
        <Route path="/upload" element={<Upload />} />
      </Routes>
    </Router>
  );
}

export default App;
