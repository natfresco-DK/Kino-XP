async function loadSeats(screeningId) {

    const response = await fetch("/screenings/" + screeningId + "/seats");

    const seats = await response.json();

    displaySeats(seats);
}


function displaySeats(seats) {

    const container = document.getElementById("seat-container");

    container.innerHTML = "";

    // Find alle rækker: A, B, C osv.
    const rows = [...new Set(seats.map(seat => seat.row))];

    // Find højeste sædenummer
    const maxSeatNumber = Math.max(
        ...seats.map(seat => seat.seatNumber)
    );


    // Vis sædenumrene øverst
    const emptyCorner = document.createElement("div");
    container.appendChild(emptyCorner);

    for (let number = 1; number <= maxSeatNumber; number++) {

        const numberElement = document.createElement("div");

        numberElement.textContent = number;
        numberElement.classList.add("seat-number");

        container.appendChild(numberElement);
    }


    // Lav hver række
    for (const row of rows) {

        // A, B, C osv.
        const rowElement = document.createElement("div");

        rowElement.textContent = row;
        rowElement.classList.add("row-name");

        container.appendChild(rowElement);


        // Sæderne i rækken
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

                // Gem seat-id på knappen til reservationsfunktionen senere
                seatElement.dataset.seatId = seat.id;

                container.appendChild(seatElement);
            }
        }
    }


    // Antal kolonner afhænger af salen
    container.style.gridTemplateColumns =
        "40px repeat(" + maxSeatNumber + ", 40px)";
}


// Find screeningId fra URL
const parts = window.location.pathname.split("/");

const screeningId = parts[2];

loadSeats(screeningId);