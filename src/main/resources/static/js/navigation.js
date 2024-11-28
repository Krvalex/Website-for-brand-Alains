// navigation.js

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

function toggleSearch() {
    const searchBar = document.getElementById('search-bar');
    searchBar.style.top = searchBar.style.top === '0px' ? '-20vh' : '0px'; // Выдвигаем или скрываем
}

// Функция поиска, которая срабатывает по клику на картинку
function searchProducts() {
    const query = document.getElementById('search-input').value;

    if (query.length >= 3) { // Проверка на минимальную длину строки
        // Перенаправляем на страницу с результатами поиска
        window.location.href = `/search?query=${encodeURIComponent(query)}`;
    } else {
        alert("Введите хотя бы 3 символа для поиска.");
    }
}

// Функция для закрытия поля поиска
function closeSearch() {
    document.getElementById('search-bar').style.top = '-20vh'; // Скрываем поле поиска
    document.getElementById('overlay').style.display = 'none'; // Убираем затемнение
}

