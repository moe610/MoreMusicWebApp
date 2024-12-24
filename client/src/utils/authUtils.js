export async function validateJwtToken(navigate) {
    const token = localStorage.getItem('jwtToken');
    const isExternalNetwork = window.location.hostname === 'moremusic.duckdns.org';
    const apiUrl = isExternalNetwork ? 'https://moremusic.duckdns.org:8443/MoreMusicWebApp' : `${import.meta.env.VITE_API_URL}`;
    const apiUrlValidate = `${apiUrl}/api/v1/auth/validate`;
    
    if (!token) {
      console.error('No token found, redirecting to login.');
      navigate("/login");
    } else {
      try {
        const response = await fetch(apiUrlValidate, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
  
        if (response.ok) {
          console.log('Token exists and is valid:', token);
        } else {
          console.error('Token is invalid or expired, redirecting to login.');
          navigate("/login");
        }
      } catch (error) {
        console.error('Error validating token:', error);
        navigate("/login");
      }
    }
}
