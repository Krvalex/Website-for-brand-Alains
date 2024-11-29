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
                const cartItem = button.closest(".cart-item");
                const quantityElement = cartItem.querySelector(".item-quantity span");
                const priceElement = cartItem.querySelector(".item-price p");

                let quantity = parseInt(quantityElement.textContent);

                quantity = action === "increment" ? quantity + 1 : quantity - 1;

                if (quantity <= 0) {
                    cartItem.remove();
                } else {
                    quantityElement.textContent = quantity;
                    const pricePerUnit = parseFloat(priceElement.getAttribute("data-price-per-unit").replace(/\s+/g, ''));
                    const newPrice = pricePerUnit * quantity;
                    priceElement.textContent = newPrice.toLocaleString('ru-RU') + " ₽";
                }

                updateCartTotal();
            } else {
                console.error(`Ошибка при обновлении ${action}:`, response.status);
            }
        })
        .catch(error => console.error("Ошибка сети:", error));
}

// Функция для пересчета общей суммы корзины
function updateCartTotal() {
    const prices = Array.from(document.querySelectorAll(".cart-item .item-price p")).map(price => {
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

        const pricePerUnit = parseFloat(priceString.replace(/\s+/g, ""));
        const totalPrice = pricePerUnit * quantity;

        // Форматируем итоговую сумму с разделением тысяч
        const formattedPrice = totalPrice.toLocaleString("ru-RU") + " ₽";

        priceElement.textContent = formattedPrice;
    });
});