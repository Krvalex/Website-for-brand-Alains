// main.js

function toggleMenu() {
    const sideMenu = document.getElementById('sideMenu');
    const menuButton = document.querySelector('.menu-button');
    const menuIcon = menuButton.querySelector('.menu-icon');
    const closeIcon = menuButton.querySelector('.close-icon');
    const overlay = document.getElementById('overlay'); // Элемент фона

    sideMenu.classList.toggle('open'); // Переключаем класс для открытия меню
    overlay.style.display = sideMenu.classList.contains('open') ? 'block' : 'none'; // Показываем/скрываем фон

    if (sideMenu.classList.contains('open')) {
        menuIcon.style.display = 'none'; // Скрыть иконку меню
        closeIcon.style.display = 'inline'; // Показать крестик
    } else {
        menuIcon.style.display = 'inline'; // Показать иконку меню
        closeIcon.style.display = 'none'; // Скрыть крестик
    }
}

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

// Функция для проверки видимости карточки
function isInViewport(element) {
    const rect = element.getBoundingClientRect();
    return rect.top < window.innerHeight && rect.bottom > window.innerHeight * 0.3; // Показываем карточку, если она на 30% видна
}

// Функция для добавления класса "visible" и применения задержки
function handleScroll() {
    const productCards = document.querySelectorAll('.product-card');
    productCards.forEach((card, index) => {
        if (isInViewport(card)) {
            // Добавляем класс "visible"
            card.classList.add('visible');

            // Устанавливаем динамическую задержку для каждой карточки
            card.style.transitionDelay = `${index * 0.015}s`; // Увеличиваем задержку с каждым индексом
        }
    });
}

// Слушаем событие прокрутки
window.addEventListener('scroll', handleScroll);

// Вызываем функцию один раз при загрузке страницы, чтобы карточки, которые уже видны, появились сразу
document.addEventListener('DOMContentLoaded', handleScroll);

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

function deleteCartItem(button) {
    const cartItemId = button.getAttribute("data-id");

    fetch(`/cart/delete/${cartItemId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.ok) {
                // Удаляем элемент из DOM
                const cartItem = button.closest(".cart-item");
                cartItem.remove();

                // Пересчитываем общую сумму корзины
                updateCartTotal();
            } else {
                console.error("Ошибка при удалении элемента из корзины:", response.status);
            }
        })
        .catch(error => console.error("Ошибка сети:", error));
}

function updateCartItemQuantity(button, action) {
    const cartItemId = button.getAttribute("data-id");

    fetch(`/cart/${action}/${cartItemId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.ok) {
                // Обновляем количество и цену элемента
                const cartItem = button.closest(".cart-item");
                const quantityElement = cartItem.querySelector(".item-quantity span");
                const priceElement = cartItem.querySelector(".item-price p");

                // Получаем текущее количество
                let quantity = parseInt(quantityElement.textContent);

                // Увеличиваем или уменьшаем количество
                quantity = action === "increment" ? quantity + 1 : quantity - 1;

                if (quantity <= 0) {
                    // Если количество стало 0, удаляем элемент
                    cartItem.remove();
                } else {
                    // Обновляем количество на странице
                    quantityElement.textContent = quantity;

                    // Обновляем цену на странице
                    const pricePerUnit = parseFloat(priceElement.getAttribute("data-price-per-unit").replace(/\s+/g, ''));
                    const newPrice = pricePerUnit * quantity;
                    priceElement.textContent = newPrice.toLocaleString('ru-RU') + " ₽";
                }

                // Пересчитываем общую сумму корзины
                updateCartTotal();
            } else {
                console.error(`Ошибка при обновлении ${action}:`, response.status);
            }
        })
        .catch(error => console.error("Ошибка сети:", error));
}

// Функция для пересчета общей суммы корзины
function updateCartTotal() {
    // Собираем все цены из элементов корзины
    const prices = Array.from(document.querySelectorAll(".cart-item .item-price p")).map(price => {
        // Убираем " ₽" и пробелы
        const priceText = price.textContent.replace(' ₽', '').replace(/\s+/g, '');
        return parseFloat(priceText);
    });

    // Суммируем все цены
    const total = prices.reduce((sum, price) => sum + price, 0);

    // Обновляем общую сумму на странице
    const totalElement = document.querySelector(".cart-total span");
    totalElement.textContent = total.toLocaleString('ru-RU') + " ₽";
}

document.addEventListener("DOMContentLoaded", () => {
    const priceElements = document.querySelectorAll(".item-price p");

    priceElements.forEach(priceElement => {
        const priceString = priceElement.getAttribute("data-price-per-unit");
        const quantity = parseInt(priceElement.closest(".cart-item").querySelector(".item-quantity span").textContent);

        // Убираем пробелы, преобразуем в число и вычисляем сумму
        const pricePerUnit = parseFloat(priceString.replace(/\s+/g, ""));
        const totalPrice = pricePerUnit * quantity;

        // Форматируем итоговую сумму с разделением тысяч
        const formattedPrice = totalPrice.toLocaleString("ru-RU") + " ₽";

        // Обновляем текст элемента
        priceElement.textContent = formattedPrice;
    });
});
