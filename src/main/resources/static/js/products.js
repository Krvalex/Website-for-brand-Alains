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