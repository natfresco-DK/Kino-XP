(function () {
    const root = document.documentElement;
    const savedTheme = localStorage.getItem("theme");
    const preferredTheme = window.matchMedia("(prefers-color-scheme: light)").matches
        ? "light"
        : "dark";

    function setTheme(theme) {
        root.dataset.theme = theme;
        const logo = document.getElementById("navbar-logo");
        const toggle = document.getElementById("themeToggle");
        if (logo) {
            logo.src = theme === "light"
                ? "/images/logo-dark.png"
                : "/images/logo.png";
        }
        if (toggle) {
            toggle.textContent = theme === "dark" ? "☀" : "☾";
            toggle.setAttribute("aria-label", theme === "dark"
                ? "Skift til lyst tema"
                : "Skift til mørkt tema");
        }
    }

    setTheme(savedTheme || preferredTheme);

    document.addEventListener("DOMContentLoaded", function () {
        const toggle = document.getElementById("themeToggle");
        if (!toggle) return;

        toggle.addEventListener("click", function () {
            const nextTheme = root.dataset.theme === "dark" ? "light" : "dark";
            localStorage.setItem("theme", nextTheme);
            setTheme(nextTheme);
        });
    });
})();
