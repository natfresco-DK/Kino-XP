const fields = ["title", "description", "genre", "duration", "ageLimit", "director", "premierDay"];

async function loadMovies() {
    const res = await fetch("/api/movies");
    const movies = await res.json();
    const list = document.getElementById("movie-list");
    list.innerHTML = "";

    movies.forEach(m => {
        const tr = document.createElement("tr");

        [m.title, m.genre, m.duration].forEach(text => {
            const td = document.createElement("td");
            td.textContent = text;
            tr .appendChild(td);
            });

        const td = document.createElement("td");
        const btn = document.createElement("button")
        btn.textContent = "Rediger";
        btn.onclick = () => openEdit(m);
        td.appendChild(btn);
        tr.appendChild(td);

        list.appendChild(tr);
    });
}

function openEdit(m) {
    document.getElementById("id").value = m.id;
    fields.forEach(f => document.getElementById(f).value = m[f]);
    document.getElementById("edit-section").hidden = false;
}

document.getElementById("edit-form").onsubmit = async  e => {
    e.preventDefault();
    const id = document.getElementById("id").value;
    const movie = {};
    fields.forEach(f => movie[f] = document.getElementById(f).value);
    movie.ageLimit = Number(movie.ageLimit);

    const res = await fetch("/api/movies/" + id, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(movie)
    });

    if (!res.ok) {
        alert("Kunne ikke gemme filmen");
        return;
    }

    document.getElementById("edit-section").hidden = true;
    loadMovies();
};

document.getElementById("cancel").onclick = () => {
    document.getElementById("edit-section").hidden = true;
};

loadMovies();