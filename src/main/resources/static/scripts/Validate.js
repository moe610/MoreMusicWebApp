const currentPath = window.location.pathname;
const contextPath = currentPath.split('/')[1];
const apiUrlLogin = `/${contextPath}/login`;

function validateJwtToken(){
    const token = localStorage.getItem("jwtToken");
    if(!token){
        console.error('No token found, redirecting to login.');
        window.location.href = apiUrlLogin;
    } else{
        console.log('Token exists:', token)
    }
}