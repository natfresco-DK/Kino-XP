// Load movies and screens on page load
document.addEventListener('DOMContentLoaded', function() {
    loadMovies();
    loadScreens();
    setMinimumDateTime();
});

// Load movies from backend
async function loadMovies() {
    try {
        const response = await fetch('/movies');
        const movies = await response.json();
        const movieSelect = document.getElementById('movieId');

        movies.forEach(movie => {
            const option = document.createElement('option');
            option.value = movie.id;
            option.textContent = movie.title;
            movieSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading movies:', error);
        document.getElementById('movieError').textContent = 'Fejl ved indlæsning af film';
        document.getElementById('movieError').style.display = 'block';
    }
}

// Load screens from backend
async function loadScreens() {
    try {
        const response = await fetch('/screens');
        const screens = await response.json();
        const screenSelect = document.getElementById('screenId');

        screens.forEach(screen => {
            const option = document.createElement('option');
            option.value = screen.id;
            option.textContent = screen.name;
            screenSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading screens:', error);
        document.getElementById('screenError').textContent = 'Fejl ved indlæsning af sale';
        document.getElementById('screenError').style.display = 'block';
    }
}

// Handle form submission
document.getElementById('screeningForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    if (!validateForm()) {
        return;
    }

    const movieId = document.getElementById('movieId').value;
    const screenId = document.getElementById('screenId').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;

    const screening = {
        movie: { id: movieId },
        screen: { id: screenId },
        startTime: startTime,
        endTime: endTime
    };

    const loading = document.getElementById('loading');
    loading.style.display = 'block';

    try {
        const response = await fetch('/screenings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(screening)
        });

        if (response.ok) {
            showSuccess();
            document.getElementById('screeningForm').reset();
            clearErrors();
        } else {
            alert('Fejl ved oprettelse af forestilling. Status: ' + response.status);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Fejl ved kommunikation med serveren');
    } finally {
        loading.style.display = 'none';
    }
});

// Validate form
function validateForm() {
    clearErrors();
    let isValid = true;

    const movieId = document.getElementById('movieId').value;
    const screenId = document.getElementById('screenId').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;

    if (!movieId) {
        showError('movieError');
        isValid = false;
    }

    if (!screenId) {
        showError('screenError');
        isValid = false;
    }

    if (!startTime) {
        showError('startTimeError');
        isValid = false;
    }

    if (!endTime) {
        showError('endTimeError');
        isValid = false;
    }

    if (startTime && endTime && new Date(startTime) >= new Date(endTime)) {
        showError('endTimeError', 'Sluttidspunktet skal være efter starttidspunktet');
        isValid = false;
    }

    return isValid;
}

// Show error message
function showError(elementId, message = null) {
    const errorElement = document.getElementById(elementId);
    errorElement.style.display = 'block';
    if (message) {
        errorElement.textContent = message;
    }
}

// Clear all error messages
function clearErrors() {
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(element => {
        element.style.display = 'none';
        element.textContent = element.getAttribute('data-original-text') || element.textContent;
    });
}

// Show success message
function showSuccess() {
    const successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'block';
    setTimeout(() => {
        successMessage.style.display = 'none';
    }, 3000);
}

// Logout handler
document.querySelector('.logout-btn').addEventListener('click', function() {
    if (confirm('Er du sikker på, at du vil logge ud?')) {
        window.location.href = '/logout';
    }
});

// Set minimum start time to now
function setMinimumDateTime() {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    document.getElementById('startTime').min = now.toISOString().slice(0, 16);
}