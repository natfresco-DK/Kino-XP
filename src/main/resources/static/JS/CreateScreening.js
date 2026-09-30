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
    const movies = getJSON("/movies");
    movies.forEach(movie => {
        const option = document.createElement("option");
        option.value = movie.id;
        option.textContent = movie.title;
        movieSelect.append(option);
    })
}

loadMovies();
