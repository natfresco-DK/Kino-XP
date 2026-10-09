const fields = ["title", "description", "genre", "duration", "actors", "releaseYear", "ageLimit"];
const listFields = ["genre", "actors"];
const csrfToken = document.querySelector('meta[name="_csrf"]').content;

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
        const movieField = field === "duration" ? "durationMinutes" : field;
        const value = listFields.includes(field) ? movie[movieField].join(", ") : movie[movieField];
        document.getElementById(field).value = value;
    });
    document.getElementById("edit-section").hidden = false;
}

function closeEdit() {
    document.getElementById("edit-section").hidden = true;
}

document.getElementById("edit-form").onsubmit = async event => {
    event.preventDefault();
    const id = document.getElementById("id").value;

    const movie = {};
    fields.forEach(field => movie[field] = document.getElementById(field).value);
    movie.duration = `PT${movie.duration}M`;

    listFields.forEach(field => {
        movie[field] = movie[field].split(",").map(genre => genre.trim()).filter(genre => genre !== "");
    });

    const res = await fetch(`/api/movies/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify(movie)
    });

    if (!res.ok) {
        const error = await res.json().catch(() => ({}));
        alert(error.message || "Kunne ikke gemme filmen");
        return;
    }

    closeEdit();
    loadMovies();
};

document.getElementById("cancel").onclick = closeEdit;
document.getElementById("cancel-form").onclick = closeEdit;
document.getElementById("edit-section").onclick = event => {
    if (event.target.id === "edit-section") closeEdit();
};
document.addEventListener("keydown", event => {
    if (event.key === "Escape") closeEdit();
});

loadMovies();