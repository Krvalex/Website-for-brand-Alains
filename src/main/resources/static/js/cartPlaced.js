document.addEventListener("DOMContentLoaded", () => {
    const timerElement = document.getElementById("redirect-timer");
    let countdown = 3;

    const interval = setInterval(() => {
        countdown--;
        if (countdown <= 0) {
            clearInterval(interval);
            window.location.href = "/"; // Редирект на главную страницу
        } else {
            timerElement.textContent = countdown;
        }
    }, 3000);
});
