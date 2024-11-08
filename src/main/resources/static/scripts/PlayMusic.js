const apiAudioFiles = `/${contextPath}/api/v1/audioFiles/files`;
const apiShuffleAudioFiles = `/${contextPath}/api/v1/audioFiles/shuffle`;
const apiAudioFilesForUserPlaylist = `/${contextPath}/api/v1/audioFiles/audioFilesForUserPlaylist`;
const apiUserNames = `/${contextPath}/api/v1/applicationUsers/userNames`;
const apiCurrentUser = `/${contextPath}/api/v1/applicationUsers/currentUser`;

const token = localStorage.getItem('jwtToken');
const audio = document.getElementById('audio-player');
const playButton = document.getElementById('play-button');
const nextButton = document.getElementById('next-button');
const previousButton = document.getElementById('previous-button');
const audioList = document.getElementById('audio-list');
const st = {};

let audioFiles = [];
let currentIndex = 0;
let skipTime = 0;
let userId = null;
let userName = null;

function togglePlay() {
    if (audio.paused) {
        audio.play();
    } else {
        audio.pause();
    }
    updatePlayButtonState();
}

function updatePlayButtonState() {
    playButton.textContent = audio.paused ? 'Play' : 'Pause';
}

function playNextAudio() {
    if (currentIndex < audioFiles.length-1){
        // Remove 'active' class from the previous item
        if (currentIndex !== null) {
            audioList.children[currentIndex].classList.remove('active');
        }
        currentIndex = currentIndex + 1;
        audioList.children[currentIndex].classList.add('active');
        loadAudioFile(currentIndex);
        skipTime = audioFiles[currentIndex].duration - 2;
    }
}

audio.addEventListener('play', () => {
    const interval = setInterval(() => {
        if (!audio.paused && audio.currentTime >= skipTime) {
            clearInterval(interval); // Stop checking
            playNextAudio();
        }
    }, 1000); // Check every second
});

function playPreviousAudio() {
    if (currentIndex > 0){
        // Remove 'active' class from the previous item
        if (currentIndex !== null) {
            audioList.children[currentIndex].classList.remove('active');
        }
        currentIndex = currentIndex - 1;
        audioList.children[currentIndex].classList.add('active');
        loadAudioFile(currentIndex);
        skipTime = audioFiles[currentIndex].duration - 2;
    }
}

function skipForward() {
    playNextAudio();
}

function skipBackward() {
    playPreviousAudio();
}

function loadAudioFile(index) {
    if (index >= 0 && index < audioFiles.length) {
        const newFile = audioFiles[index];
        const audioSource = document.getElementById('audio-source');
        audioSource.src = '/' + contextPath + '/MusicFiles/' + encodeURIComponent(newFile.fileName);
        document.title = newFile.title;
        audio.load();
        audio.play().catch(error => console.error('Error playing audio:', error));
        updateMediaSessionMetadata(newFile);
    } else {
        console.error('Invalid index:', index);
    }
}

function updateMediaSessionMetadata(audioFile) {
    if ('mediaSession' in navigator) {
        navigator.mediaSession.metadata = new MediaMetadata({
            title: audioFile.title || "Track Title",
            artist: audioFile.artist || "Artist Name",
            album: audioFile.album || "Album Name",
        });
        document.title = audioFile.title;

        navigator.mediaSession.setActionHandler('nexttrack', () => {
            skipForward();
        });
        navigator.mediaSession.setActionHandler('previoustrack', () => {
            skipBackward();
        });
        navigator.mediaSession.setActionHandler('play', () => {
            togglePlay();
        });
        navigator.mediaSession.setActionHandler('pause', () => {
            togglePlay();
        });
    }
}

audio.addEventListener('ended', () => {
    nextButton.click(); // Simulate user click on the next button when audio ends
});

function audioFileMapping(files){
    audioFiles = files;
    currentIndex = 0;

    playButton.addEventListener('click', togglePlay);
    nextButton.addEventListener('click', playNextAudio);
    previousButton.addEventListener('click', playPreviousAudio);
    // Clear all existing items in the list
    audioList.innerHTML = ""; // or use audioList.replaceChildren();z

    // Populate the scrollable list
    audioFiles.forEach((file, index) => {
        const listItem = document.createElement('div');
        listItem.className = 'audio-item';
        listItem.textContent = file.title; // Display the title
        listItem.onclick = () => {
            // Remove 'active' class from the previous item
            if (currentIndex !== null) {
                audioList.children[currentIndex].classList.remove('active');
            }
            currentIndex = index; // Set the current index
            listItem.classList.add('active'); // Highlight the selected item
            loadAudioFile(currentIndex); // Load the selected audio file
            skipTime = audioFiles[currentIndex].duration - 2;
        };
        audioList.appendChild(listItem); // Add item to the list
    });

    // Initialize media session metadata
    if (audioFiles.length > 0) {
        audioList.children[currentIndex].classList.add('active');
        loadAudioFile(currentIndex);
        skipTime = audioFiles[currentIndex].duration - 2;
    }
}

// toggle flap logic
st.flap = document.querySelector('#flap');
st.toggle = document.querySelector('.toggle');

st.playToggle = document.querySelector('#play-toggle');
st.shuffleToggle = document.querySelector('#shuffle-toggle');

st.flap.addEventListener('transitionend', () => {
    if (st.playToggle.checked) {
        st.toggle.style.transform = 'rotateY(-15deg)';
        setTimeout(() => st.toggle.style.transform = '', 400);
    } else {
        st.toggle.style.transform = 'rotateY(15deg)';
        setTimeout(() => st.toggle.style.transform = '', 400);
    }
})

st.clickHandler = (shuffleToggle) => {
    if (shuffleToggle.target.tagName === 'LABEL') {
        setTimeout(() => {
            st.flap.children[0].textContent = shuffleToggle.target.textContent;
        }, 250);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    st.flap.children[0].textContent = st.playToggle.nextElementSibling.textContent;
});

document.addEventListener('click', (shuffleToggle) => st.clickHandler(shuffleToggle));

document.getElementById('shuffle-toggle').addEventListener('change', function() {
    shuffleAudioFiles(this.checked);
});
document.getElementById('play-toggle').addEventListener('change', function() {
    shuffleAudioFiles(!this.checked);
});

function shuffleAudioFiles(shuffleOn){
    fetch(`${apiShuffleAudioFiles}/${userId}/${shuffleOn}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch audio files');
            }
            return response.json();
        })
        .then(files => {
            audioFileMapping(files);
        })
        .catch(error => console.error('Error fetching audio files:', error));
}


//playlist dropdown
function show(username) {
    document.querySelector('.playlist').value = username;
}

async function populateDropdown(){
    try {
        const response = await fetch(apiUserNames);
        const users = await response.json();

        // Select the options container
        const optionsContainer = document.getElementById('playlist-options');

        // Clear any existing options
        optionsContainer.innerHTML = '';

        // Create a div for each user and add it to the options container
        users.forEach(user => {
            const option = document.createElement('div');
            option.textContent = user.username; // Assuming 'username' is the field you want
            option.onmouseover = () => show(user.username);

            // When clicking an option, set userId and username, then fetch audio files
            option.onclick = () => {
                userId = user.id;
                userName = user.username;
                fetchAudioFilesForUser();
            };

            optionsContainer.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching user playlists:', error);
    }
}

let dropdown = document.querySelector('.dropdown');
dropdown.onclick = function(){
    dropdown.classList.toggle('active');
}

// Call populateDropdown when the page loads
document.addEventListener('DOMContentLoaded', populateDropdown);

// get playlist for user selected in dropdown
function fetchAudioFilesForUser(){
    fetch(`${apiAudioFilesForUserPlaylist}/${userId}`, { // Append the userId to the API URL
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch audio files');
            }
            return response.json();
        })
        .then(files => {
            audioFileMapping(files);
        })
        .catch(error => console.error('Error fetching audio files:', error));
}

if (token) {
    fetch(apiAudioFiles, {
        headers: {
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        }
    })
    .then(response => {
        if(!response.ok){
            throw new Error('Failed to fetch audio files');
        }
        return response.json();
    })
    .then(files => {
        audioFileMapping(files);
    })
    .catch(error => console.error('Error fetching audio files:', error));

    fetch(apiCurrentUser, {
        headers: {
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        }
    })
        .then(response => {
            if(!response.ok){
                throw new Error('Failed to fetch audio files');
            }
            return response.json();
        })
        .then(user => {
            userId = user.id;
            userName = user.username;
        })
        .catch(error => console.error('Error fetching current user:', error));
} else{
    console.error("No JWT token found in localstorage");
}
