document.addEventListener("DOMContentLoaded", () => {
    const stockErrorNotification = document.getElementById("stockErrorNotification");
    if (stockErrorNotification) {
        showNotification("stockErrorNotification");
    }
});

function showNotification(notificationId) {
    const notification = document.getElementById(notificationId);
    if (notification) {
        notification.classList.add("show");
        setTimeout(() => {
            closeNotification(notificationId);
        }, 5000); // Автоматически скрываем через 5 секунд
    }
}

function closeNotification(notificationId) {
    const notification = document.getElementById(notificationId);
    if (notification) {
        notification.classList.remove("show");
    }
}
