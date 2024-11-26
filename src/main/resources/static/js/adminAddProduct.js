function addSizeField() {
    const container = document.getElementById("sizeFieldsContainer");
    const div = document.createElement("div");
    div.classList.add("size-field");

    // Создание выпадающего списка для выбора размера
    const sizeSelect = document.createElement("select");
    sizeSelect.name = "sizes";
    sizeSelect.required = true;
    sizeSelect.innerHTML = `
        <option value="XS">XS</option>
        <option value="S">S</option>
        <option value="M">M</option>
        <option value="L">L</option>
        <option value="XL">XL</option>
        <option value="XXL">XXL</option>
    `;
    div.appendChild(sizeSelect);

    // Создание поля ввода для количества
    const quantityInput = document.createElement("input");
    quantityInput.type = "number";
    quantityInput.name = "quantities";
    quantityInput.required = true;
    quantityInput.placeholder = "Количество";
    div.appendChild(quantityInput);

    // Добавление кнопки для удаления этого поля
    const deleteButton = document.createElement("button");
    deleteButton.type = "button";
    deleteButton.textContent = "Удалить";
    deleteButton.onclick = () => container.removeChild(div);
    div.appendChild(deleteButton);

    container.appendChild(div);
}
