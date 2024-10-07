const apiUrl = `/${contextPath}/api/v1/uploadAudioFiles`;
const token = localStorage.getItem('jwtToken')

document.getElementById('submitButton').addEventListener('click', function () {
    const youtubeUrl = document.getElementById('youtubeUrl').value;
    const messageElement = document.getElementById('message');
    const submitButton = document.getElementById('submitButton');

    messageElement.textContext = '';
    messageElement.style.display = 'none';

    if (!youtubeUrl) {
        messageElement.textContent = 'Please enter a YouTube URL.';
        messageElement.className = 'alert alert-danger mt-3';
        messageElement.style.display = 'block';
        return;
    }

    submitButton.disabled = true;
    submitButton.classList.add('disabled');


    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ youtubeUrl: youtubeUrl })
    })
        .then(response => {
            if (response.ok) {
                messageElement.textContent = 'Audio file uploaded successfully!';
                messageElement.className = 'alert alert-success mt-3';
            } else {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
        })
        .catch(error => {
            messageElement.textContent = `Error: ${error.message}`;
            messageElement.className = 'alert alert-danger mt-3';
        })
        .finally(() => {
            messageElement.style.display = 'block';
            submitButton.disabled = false;
            submitButton.classList.remove('disabled');
        });
});