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

function toggleFavorite(element) {
    const productId = element.getAttribute("data-product-id");
    const isActive = element.classList.contains("active");
    const heartIconImage = element.querySelector("img"); // Получаем изображение внутри сердечка
    const url = isActive
        ? `/favorites/remove/${productId}` // URL для удаления
        : `/favorites/${productId}`; // URL для добавления

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then(response => {
            if (response.ok) {
                // Переключаем класс активного сердечка
                element.classList.toggle("active");

                // Меняем изображение в зависимости от состояния
                if (element.classList.contains("active")) {
                    heartIconImage.src = "/images/various/favorites-active.png"; // Изображение для активного состояния
                } else {
                    heartIconImage.src = "/images/various/favorites.png"; // Исходное изображение
                }
            } else {
                alert("Не удалось обновить избранное. Попробуйте снова.");
            }
        });
}

const currentProductId = window.location.pathname.split('/').pop();

document.querySelectorAll('.star').forEach(star => {
    star.addEventListener('click', function () {
        const rating = parseInt(this.dataset.rating);

        // Удаляем все активные классы
        document.querySelectorAll('.star').forEach(s => s.classList.remove('active'));

        // Добавляем активный класс до выбранной звезды
        document.querySelectorAll('.star').forEach((s, index) => {
            if (index < rating) {
                s.classList.add('active');
            }
        });
    });
});

function submitReview(e) {
    e.preventDefault();
    const form = e.target;
    const rating = document.querySelectorAll('.star.active').length;

    if (rating === 0) {
        alert('Выберите рейтинг!');
        return;
    }

    fetch(`/reviews/add?productId=${currentProductId}&text=${encodeURIComponent(form.querySelector('textarea').value)}&rating=${rating}`, {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                window.location.reload(); // Принудительная перезагрузка
            } else {
                alert('Ошибка при отправке отзыва');
            }
        });
}