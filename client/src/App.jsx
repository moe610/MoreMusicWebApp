import { Routes, Route, BrowserRouter as Router } from 'react-router-dom';
import Home from './Home';
import Music from './MusicPage';
import Upload from './UploadPage';
import Login from './LoginPage';
import Register from './RegisterPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/music" element={<Music />} />
        <Route path="/upload" element={<Upload />} />
      </Routes>
    </Router>
  );
}

export default App;
