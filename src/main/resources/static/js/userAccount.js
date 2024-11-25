function toggleDetails(element) {
    const details = element.parentElement.nextElementSibling; // Находим блок с деталями
    const arrow = element.querySelector('.arrow'); // Находим стрелку внутри элемента

    if (details.style.display === "none" || details.style.display === "") {
        details.style.display = "block"; // Показываем детали
        arrow.classList.add('open'); // Поворачиваем стрелку вверх
        arrow.classList.remove('closed'); // Убираем класс "закрыто"
    } else {
        details.style.display = "none"; // Скрываем детали
        arrow.classList.add('closed'); // Возвращаем стрелку вниз
        arrow.classList.remove('open'); // Убираем класс "открыто"
    }
}