
function toggleFilmMenu() {

    const menu = document.querySelector("#filmMenu");
    const arrow = document.querySelector("#filmArrow");

    menu.classList.toggle("open");
    arrow.classList.toggle("rotated");
}

function toggleKioskMenu() {
//
    const menu = document.querySelector("#kioskMenu");
    const arrow = document.querySelector("#kioskArrow");

    menu.classList.toggle("open");
    arrow.classList.toggle("rotated");
}