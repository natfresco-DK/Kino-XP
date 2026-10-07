async function loadSeats(screeningId) {

    const container = document.getElementById("seat-container");

    try {

        const response = await fetch(
            "/screenings/" + screeningId + "/seats"
        );

        if (!response.ok) {
            throw new Error("Kunne ikke hente sæder");
        }

        const seats = await response.json();

        if (!Array.isArray(seats)) {
            throw new Error("Ugyldigt svar fra serveren");
        }

        displaySeats(seats);

    } catch (error) {

        container.innerHTML =
            "<p>Kunne ikke hente sæderne.</p>";
    }
}


function displaySeats(seats) {

    const container = document.getElementById("seat-container");

    container.innerHTML = "";

    const rows = [...new Set(seats.map(seat => seat.row))].sort();

    const maxSeatNumber = Math.max(
        ...seats.map(seat => seat.seatNumber)
    );


    const emptyCorner = document.createElement("div");
    container.appendChild(emptyCorner);

    for (let number = 1; number <= maxSeatNumber; number++) {

        const numberElement = document.createElement("div");

        numberElement.textContent = number;
        numberElement.classList.add("seat-number");

        container.appendChild(numberElement);
    }


    for (const row of rows) {

        const rowElement = document.createElement("div");

        rowElement.textContent = row;
        rowElement.classList.add("row-name");

        container.appendChild(rowElement);


        for (let number = 1; number <= maxSeatNumber; number++) {

            const seat = seats.find(
                seat =>
                    seat.row === row &&
                    seat.seatNumber === number
            );

            if (seat) {

                const seatElement = document.createElement("button");

                seatElement.classList.add("seat");

                if (seat.reserved) {
                    seatElement.classList.add("reserved");
                    seatElement.disabled = true;
                } else {
                    seatElement.classList.add("available");
                }

                seatElement.setAttribute(
                    "aria-label",
                    "Sæde " + seat.row + seat.seatNumber +
                    ", " + (seat.reserved ? "reserveret" : "ledig")
                );

                seatElement.dataset.seatId = seat.id;

                container.appendChild(seatElement);
            }
        }
    }


    container.style.gridTemplateColumns =
        "40px repeat(" + maxSeatNumber + ", 40px)";
}


const parts = window.location.pathname.split("/");

const screeningId = parts[2];

loadSeats(screeningId);