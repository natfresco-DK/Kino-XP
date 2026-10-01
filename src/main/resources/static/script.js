const movieForm = document.querySelector("#movieForm");
const submitButton = movieForm.querySelector("button[type='submit']");

movieForm.addEventListener("submit", function(event) {

    event.preventDefault();

    if (submitButton.disabled) {
        return;
    }

    submitButton.disabled = true;

    const movie = {
        title: document.querySelector("#title").value,

        description: document.querySelector("#description").value,

        genre: document.querySelector("#genre")
            .value
            .split(",")
            .map(genre => genre.trim()),

        duration: Number(document.querySelector("#duration").value),

        actors: document.querySelector("#actors")
            .value
            .split(",")
            .map(actor => actor.trim()),

        releaseYear: Number(document.querySelector("#releaseYear").value),

        ageLimit: document.querySelector("#ageLimit").value
    };

    fetch("/movies", {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(movie)
    })

        .then(response => {

            if (!response.ok) {
                return response.json()
                    .catch(() => ({}))
                    .then(body => {
                        throw new Error(
                            body.message || "Filmen kunne ikke oprettes."
                        );
                    });
            }

            return response.json();
        })

        .then(data => {

            console.log("Film oprettet:", data);

            alert("Filmen er blevet oprettet!");

            movieForm.reset();
        })

        .catch(error => {

            console.log("Fejl:", error);

            alert(error.message);
        })

        .finally(() => {

            submitButton.disabled = false;
        });
});