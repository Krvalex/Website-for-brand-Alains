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