const form = document.querySelector("#screeningForm");
const movieSelect = document.querySelector("#movie");
const screenSelect = document.querySelector("#screen");
const startTime = document.querySelector("#startTime");
const endTime = document.querySelector("#endTime");
const statusMessage = document.querySelector("#status");

async function getJSON(url){
    const response = await fetch(url);
    if(!response.ok){
        throw new Error("Request failed (" + response.status + ").");
    }

    return response.json();

}

async function loadMovies(){
    try{
    const movies = await getJSON("/movies");
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

form.addEventListener("submit", event =>{
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
})

const response = await fetch("/screenings", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify(screening)
});
if(!response.ok){
    throw new Error("Requst failed (" + response.status + ")");
}
