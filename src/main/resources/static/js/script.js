const movieForm = document.querySelector("#movieForm");
const submitButton = movieForm.querySelector("button[type='submit']");
const message = document.querySelector("#message");
const csrfToken = document.querySelector('meta[name="_csrf"]').content;

movieForm.addEventListener("submit", function(event) {

    event.preventDefault();

    if (submitButton.disabled) {
        return;
    }

    submitButton.disabled = true;
    message.textContent = "";

    const movie = {
        title: document.querySelector("#title").value,

        description: document.querySelector("#description").value,

        genre: document.querySelector("#genre")
            .value
            .split(",")
            .map(genre => genre.trim()),

        duration: "PT" + document.querySelector("#duration").value + "M",

        
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
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfToken
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

            message.textContent = "Filmen er blevet oprettet!";

            movieForm.reset();
        })

        .catch(error => {

            console.log("Fejl:", error);

            message.textContent = error.message;
        })

        .finally(() => {

            submitButton.disabled = false;
        });
});