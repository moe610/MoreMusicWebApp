export async function validateJwtToken(navigate) {
    const token = localStorage.getItem('jwtToken');
    const apiUrlValidate = `${import.meta.env.VITE_API_URL}/api/v1/auth/validate`;
    const urlLogin = `${import.meta.env.VITE_BASE_URL}login`;
    
    if (!token) {
      console.error('No token found, redirecting to login.');
      navigate(urlLogin);
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
          navigate(urlLogin);
        }
      } catch (error) {
        console.error('Error validating token:', error);
        navigate(urlLogin);
      }
    }
}
