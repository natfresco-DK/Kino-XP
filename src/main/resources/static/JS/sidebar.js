
function toggleFilmMenu() {

    const menu = document.querySelector("#filmMenu");
    const arrow = document.querySelector("#filmArrow");

    menu.classList.toggle("open");
    arrow.classList.toggle("rotated");
}