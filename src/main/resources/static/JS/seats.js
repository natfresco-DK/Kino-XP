const container = document.getElementById("seat-container");
const reservationForm = document.getElementById("reservationForm");
const phoneInput = document.getElementById("phoneNumber");
const reserveButton = document.getElementById("reserveButton");
const selectedSeatsText = document.getElementById("selectedSeats");
const reservationStatus = document.getElementById("reservationStatus");

const selectedSeats = new Map();

const parts = window.location.pathname.split("/");
const screeningId = Number(parts[2]);

async function readErrorMessage(response, fallback) {
    try {
        const data = await response.json();
        return data.message || fallback;
    } catch (e) {
        return fallback;
    }
}

function updateSelection() {
    if (selectedSeats.size === 0) {
        selectedSeatsText.textContent = "Ingen sæder valgt";
    } else {
        selectedSeatsText.textContent =
            "Valgte sæder: " + [...selectedSeats.values()].join(", ");
    }
    reserveButton.disabled = selectedSeats.size === 0;
}

function toggleSeat(seat, seatElement) {
    const label = seat.row + seat.seatNumber;

    if (selectedSeats.has(seat.id)) {
        selectedSeats.delete(seat.id);
        seatElement.classList.remove("selected");
        seatElement.setAttribute("aria-pressed", "false");
    } else {
        selectedSeats.set(seat.id, label);
        seatElement.classList.add("selected");
        seatElement.setAttribute("aria-pressed", "true");
    }

    reservationStatus.textContent = "";
    updateSelection();
}

async function loadSeats() {
    try {
        const response = await fetch("/screenings/" + screeningId + "/seats");

        if (!response.ok) {
            throw new Error("Kunne ikke hente sæder");
        }

        const seats = await response.json();

        if (!Array.isArray(seats)) {
            throw new Error("Ugyldigt svar fra serveren");
        }

        displaySeats(seats);

    } catch (error) {
        container.innerHTML = "<p>Kunne ikke hente sæderne.</p>";
    }
}

function displaySeats(seats) {
    container.innerHTML = "";

    if (seats.length === 0) {
        const message = document.createElement("p");
        message.textContent = "Der er ingen sæder i denne sal.";
        container.appendChild(message);
        return;
    }

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

            if (!seat) {
                container.appendChild(document.createElement("div"));
                continue;
            }

            const seatElement = document.createElement("button");
            seatElement.type = "button";
            seatElement.classList.add("seat");
            seatElement.dataset.seatId = seat.id;

            if (seat.reserved) {
                seatElement.classList.add("reserved");
                seatElement.disabled = true;
                seatElement.setAttribute("aria-label", "Sæde " + row + number + ", reserveret");
            } else {
                seatElement.classList.add("available");
                seatElement.setAttribute("aria-label", "Sæde " + row + number + ", ledig");
                seatElement.setAttribute("aria-pressed", "false");
                seatElement.addEventListener("click", () => toggleSeat(seat, seatElement));
            }

            container.appendChild(seatElement);
        }
    }

    container.style.gridTemplateColumns =
        "40px repeat(" + maxSeatNumber + ", 40px)";
}

reservationForm.addEventListener("submit", async event => {
    event.preventDefault();
    reservationStatus.textContent = "";

    if (selectedSeats.size === 0) {
        reservationStatus.textContent = "Vælg mindst ét sæde.";
        return;
    }

    reserveButton.disabled = true;

    const seatLabels = [...selectedSeats.values()].join(", ");
    const request = {
        screeningId: screeningId,
        seatIds: [...selectedSeats.keys()],
        phoneNumber: phoneInput.value.trim()
    };

    try {
        const response = await fetch("/reservations", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            reservationStatus.textContent =
                await readErrorMessage(response, "Reservationen kunne ikke gennemføres.");

            if (response.status === 409) {
                selectedSeats.clear();
                updateSelection();
                await loadSeats();
            }
            return;
        }

        reservationStatus.textContent =
            "Reserveret: " + seatLabels + " til " + request.phoneNumber;

        selectedSeats.clear();
        reservationForm.reset();
        updateSelection();
        await loadSeats();

    } catch (e) {
        reservationStatus.textContent = "Kunne ikke kontakte serveren.";
    } finally {
        reserveButton.disabled = selectedSeats.size === 0;
    }
});

if (!Number.isInteger(screeningId) || screeningId <= 0) {
    container.innerHTML = "<p>Ugyldig forestilling.</p>";
    reservationForm.hidden = true;
} else {
    updateSelection();
    loadSeats();
}