const movieSelect = document.getElementById("movie");
const dateInput = document.getElementById("date");
const showAllBtn = document.getElementById("showAllBtn");
const screeningList = document.getElementById("screeningList");
const statusMessage = document.getElementById("status");

function formatDate(dateTime) {
    return new Date(dateTime).toLocaleDateString("da-DK", {
        weekday: "long", day: "numeric", month: "long"
    });
}

function formatTime(dateTime) {
    return new Date(dateTime).toLocaleTimeString("da-DK", {
        hour: "2-digit", minute: "2-digit"
    });
}

function renderScreenings(screenings) {
    screeningList.innerHTML = "";

    if (screenings.length === 0) {
        statusMessage.textContent = "Der er ingen forestillinger.";
        return;
    }

    statusMessage.textContent = "";

    screenings.forEach(screening => {
        const row = document.createElement("tr");

        [
            formatDate(screening.startTime),
            formatTime(screening.startTime) + " – " + formatTime(screening.endTime),
            screening.movieTitle,
            screening.screenName
        ].forEach(value => {
            const cell = document.createElement("td");
            cell.textContent = value;
            row.appendChild(cell);
        });

        screeningList.appendChild(row);
    });
}

async function loadScreenings(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error();
        }
        renderScreenings(await response.json());
    } catch (e) {
        screeningList.innerHTML = "";
        statusMessage.textContent = "Kunne ikke hente programmet.";
    }
}

async function loadMovies() {
    try {
        const response = await fetch("/api/movies");
        const movies = await response.json();

        movies.forEach(movie => {
            const option = document.createElement("option");
            option.value = movie.id;
            option.textContent = movie.title;
            movieSelect.appendChild(option);
        });
    } catch (e) {
        statusMessage.textContent = "Kunne ikke hente film.";
    }
}

movieSelect.addEventListener("change", () => {
    dateInput.value = "";
    if (movieSelect.value) {
        loadScreenings("/screenings/movie/" + movieSelect.value);
    } else {
        loadScreenings("/screenings");
    }
});

dateInput.addEventListener("change", () => {
    movieSelect.value = "";
    if (dateInput.value) {
        loadScreenings("/screenings/date/" + dateInput.value);
    } else {
        loadScreenings("/screenings");
    }
});

showAllBtn.addEventListener("click", () => {
    movieSelect.value = "";
    dateInput.value = "";
    loadScreenings("/screenings");
});

loadMovies();
loadScreenings("/screenings");