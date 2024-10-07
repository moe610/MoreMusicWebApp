const apiUrl = `/${contextPath}/api/v1/audioFiles`;
const token = localStorage.getItem('jwtToken');
const audio = document.getElementById('audio-player');
const playButton = document.getElementById('play-button');
const nextButton = document.getElementById('next-button');
const previousButton = document.getElementById('previous-button');

let audioFiles = [];
let shuffledIndices = [];
let currentIndex = 0;
let currentShuffleIndex = 0;

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
    currentShuffleIndex = (currentShuffleIndex + 1) % shuffledIndices.length;
    currentIndex = shuffledIndices[currentShuffleIndex];
    loadAudioFile(currentIndex);
}

function playPreviousAudio() {
    currentShuffleIndex = (currentShuffleIndex - 1 + shuffledIndices.length) % shuffledIndices.length;
    currentIndex = shuffledIndices[currentShuffleIndex];
    loadAudioFile(currentIndex);
}

function loadAudioFile(index) {
    if (index >= 0 && index < audioFiles.length) {
        const newFile = audioFiles[index];
        const audioSource = document.getElementById('audio-source');
        audioSource.src = '/' + contextPath + '/MusicFiles/' + encodeURIComponent(newFile.fileName); //remove MoreMusicWebApp just for testing

        document.title = newFile.title;

        audio.load();
        audio.play().catch(error => console.error('Error playing audio:', error));
        updateMediaSessionMetadata(newFile);
    } else {
        console.error('Invalid index:', index);
    }
}

function shuffleArrayIndices(length) {
    let indices = Array.from({ length }, (_, index) => index);
    for (let i = indices.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [indices[i], indices[j]] = [indices[j], indices[i]];
    }
    return indices;
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

function skipForward() {
    playNextAudio();
}

function skipBackward() {
    playPreviousAudio();
}

audio.addEventListener('ended',playNextAudio)


if (token) {
    fetch(apiUrl, {
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
    audioFiles = files;
    shuffledIndices = shuffleArrayIndices(audioFiles.length);
    currentIndex = 0;
    currentShuffleIndex = 0;

    playButton.addEventListener('click', togglePlay);
    nextButton.addEventListener('click', playNextAudio);
    previousButton.addEventListener('click', playPreviousAudio);

    // Populate the scrollable list
    const audioList = document.getElementById('audio-list');
    audioFiles.forEach((file, index) => {
        const listItem = document.createElement('div');
        listItem.className = 'audio-item';
        listItem.textContent = file.title; // Display the title
        listItem.onclick = () => {
            currentIndex = index; // Set the current index
            loadAudioFile(currentIndex); // Load the selected audio file
        };
        audioList.appendChild(listItem); // Add item to the list
    });

    // Initialize media session metadata
    if (audioFiles.length > 0) {
        loadAudioFile(currentIndex);
    }
})
    .catch(error => console.error('Error fetching audio files:', error));
} else{
    console.error("No JWT token found in localstorage");
}