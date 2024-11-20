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


