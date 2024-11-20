document.addEventListener('DOMContentLoaded', () => {
    const sizeButtons = document.querySelectorAll('.size-option');
    const selectedSizeInput = document.getElementById('selected-size');

    sizeButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Убираем выделение со всех кнопок
            sizeButtons.forEach(btn => btn.classList.remove('selected'));
            // Выделяем выбранную кнопку
            button.classList.add('selected');
            // Устанавливаем значение выбранного размера в скрытое поле формы
            selectedSizeInput.value = button.getAttribute('data-size');
        });
    });
});

// Функция для показа уведомления
function showNotification(notificationId) {
    const notification = document.getElementById(notificationId);
    notification.classList.add('show');

    // Автоматически скрываем уведомление через 3 секунды
    setTimeout(() => {
        closeNotification(notificationId);
    }, 3000);
}

// Функция для скрытия уведомления
function closeNotification(notificationId) {
    const notification = document.getElementById(notificationId);
    notification.classList.remove('show');
}

document.querySelector('.add-to-cart-btn').addEventListener('click', function (event) {
    const form = this.closest('form'); // Находим форму
    const selectedSize = document.getElementById('selected-size').value;

    // Проверяем, выбран ли размер
    if (!selectedSize) {
        event.preventDefault(); // Предотвращаем отправку формы
        showNotification('errorNotification'); // Показываем уведомление об ошибке
        return;
    }

    // Перехватываем форму перед её отправкой
    event.preventDefault();

    // Выполняем стандартный редирект в фоне
    fetch(form.action, {
        method: 'POST',
        body: new FormData(form), // Отправляем данные формы
    })
        .then(response => {
            if (response.redirected) {
                // Если сервер отправил редирект, обрабатываем это
                showNotification('successNotification'); // Показываем уведомление об успехе
            } else {
                throw new Error('Ошибка при добавлении в корзину');
            }
        })
        .catch(error => {
            console.error(error);
            showNotification('errorNotification'); // Показываем уведомление об ошибке
        });
});