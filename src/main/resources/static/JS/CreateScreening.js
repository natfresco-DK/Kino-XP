const form = document.querySelector("#screeningForm");
const movieSelect = document.querySelector("#movie");
const screenSelect = document.querySelector("#screen");
const startTime = document.querySelector("#startTime");
const endTime = document.querySelector("#endTime");
const statusMessage = document.querySelector("#status");
const screeningList = document.querySelector("#screeningList");

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

async function loadScreenings() {
    try {
        const screenings = await getJSON("/screenings");
        renderScreenings(screenings);
    } catch (error) {
        console.error(error);
        screeningList.innerHTML = "";
        statusMessage.textContent = "Kan ikke hente forestillinger";
    }
}

async function getJSON(url){
    const response = await fetch(url);
    if(!response.ok){
        throw new Error("Request failed (" + response.status + ").");
    }

    return response.json();

}

async function loadMovies(){
    try{
    const movies = await getJSON("/api/movies");
    movies.forEach(movie => {
        const option = document.createElement("option");
        option.value = movie.id;
        option.textContent = movie.title;
        movieSelect.append(option);
    })} catch (error){
        console.error(error)
        statusMessage.textContent = "Kan ikke hente movies";
    }
}

async function loadScreens(){
    try {
        const screens = await getJSON("/screens");
        console.log(screens)
        screens.forEach(screen => {
            const option = document.createElement("option");
            option.value = screen.id;
            option.textContent = screen.name;
            screenSelect.append(option);
        })
    } catch (error){
        console.error(error);
        statusMessage.textContent = "Kan ikke hente sale";
    }
}

loadScreens();
loadMovies();
loadScreenings();

form.addEventListener("submit", async event => {
    event.preventDefault();

    const screenId = screenSelect.value;
    const movieId = movieSelect.value;
    const start = startTime.value;
    const end = endTime.value;

    const screening = {
        movieId: movieId,
        screenId: screenId,
        startTime: start,
        endTime: end
    };
    if(end <= start){
        statusMessage.textContent = "Slut tidspunk skal være efter start tidspunkt";
        return;
    }
    try {
        statusMessage.textContent = "Opretter forestilling";
        const response = await fetch("/screenings", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(screening)
        });

        if (!response.ok) {
            throw new Error(
                "Request failed (" + response.status + ")"
            );
        }

        statusMessage.textContent = "Forestilling oprettet";
        form.reset();
        loadScreenings();

    } catch (error) {
        console.error(error);
        statusMessage.textContent = "Kunne ikke oprette forestilling";
    }
});