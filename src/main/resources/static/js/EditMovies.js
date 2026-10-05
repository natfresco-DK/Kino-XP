const fields = ["title", "description", "genre", "duration", "actors", "releaseYear", "ageLimit"];
const listFields = ["genre", "actors"];

async function loadMovies() {
    const res = await fetch("/api/movies");
    const movies = await res.json();
    const list = document.getElementById("movie-list");
    list.innerHTML = "";

    movies.forEach(movie => {
        const tr = document.createElement("tr");

        [movie.title, movie.description, movie.genre.join(", "), movie.durationMinutes, movie.actors.join(", "), movie.releaseYear, movie.ageLimit].forEach(text => {
            const td = document.createElement("td");
            td.textContent = text;
            tr.appendChild(td);
        });

        const td = document.createElement("td");
        const btn = document.createElement("button");
        btn.textContent = "Rediger";
        btn.onclick = () => openEdit(movie);
        td.appendChild(btn);
        tr.appendChild(td);

        list.appendChild(tr);
    });
}

function openEdit(movie) {
    document.getElementById("id").value = movie.id;
    fields.forEach(field => {
        const value = listFields.includes(field) ? movie[field].join(", ") : movie[field];
        document.getElementById(field).value = value;
    });
    document.getElementById("edit-section").hidden = false;
}

document.getElementById("edit-form").onsubmit = async event => {
    event.preventDefault();
    const id = document.getElementById("id").value;

    const movie = {};
    fields.forEach(field => movie[field] = document.getElementById(field).value);

    listFields.forEach(field => {
        movie[field] = movie[field].split(",").map(genre => genre.trim()).filter(genre => genre !== "");

    });

    if (!res.ok) {
        const error = await res.json().catch(() => ({}));
        alert(error.message || "Kunne ikke gemme filmen");
        return;
    }

    document.getElementById("edit-section").hidden = true;
    loadMovies();
};

document.getElementById("cancel").onclick = () => {
    document.getElementById("edit-section").hidden = true;
};

loadMovies();