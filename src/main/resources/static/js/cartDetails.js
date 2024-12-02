document.addEventListener("DOMContentLoaded", () => {
    const applyPromoButton = document.querySelector(".apply-promo-btn");
    const promoCodeInput = document.querySelector("#promo-code");
    const originalSumElement = document.querySelector(".cart-summary span");
    const totalSumInput = document.querySelector("#totalSum");
    const discountInfo = document.querySelector(".discount-info");
    const discountPercentageElement = document.querySelector("#discountPercentage");

    // Устанавливаем итоговую сумму при загрузке страницы
    if (originalSumElement && totalSumInput) {
        totalSumInput.value = originalSumElement.textContent.replace(" ₽", "").replace(/\s+/g, "");
    }

    applyPromoButton.addEventListener("click", () => {
        const promoCode = promoCodeInput.value.trim();
        let originalSumText = originalSumElement.textContent.replace(" ₽", "").replace(/\s+/g, "");
        const originalSum = parseFloat(originalSumText);

        if (promoCode !== "") {
            fetch("/promo/apply", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: `code=${encodeURIComponent(promoCode)}&originalSum=${originalSum}`,
            })
                .then((response) => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error("Промокод недействителен");
                    }
                })
                .then((discountedSum) => {
                    const discountPercentage = calculateDiscountPercentage(originalSum, discountedSum);

                    // Обновляем отображение итоговой суммы и скидки
                    originalSumElement.textContent = formatNumberWithSpaces(discountedSum) + " ₽";
                    totalSumInput.value = discountedSum;

                    // Показать скидку
                    discountPercentageElement.textContent = discountPercentage;
                    discountInfo.style.display = "block"; // Отображаем скидку

                    showNotification("promoSuccessNotification");

                    // Блокируем поле и кнопку
                    promoCodeInput.disabled = true;
                    applyPromoButton.disabled = true;
                    applyPromoButton.textContent = "Промокод применен";
                    applyPromoButton.style.backgroundColor = "#888";
                })
                .catch((error) => {
                    showNotification("promoErrorNotification");
                });
        } else {
            showNotification("promoErrorNotification");
        }
    });

    function calculateDiscountPercentage(originalSum, discountedSum) {
        return Math.round(((originalSum - discountedSum) / originalSum) * 100);
    }

    function formatNumberWithSpaces(number) {
        return number
            .toFixed(2)
            .replace(/\B(?=(\d{3})+(?!\d))/g, " ")
            .replace(".00", "");
    }

    function showNotification(notificationId) {
        const notification = document.getElementById(notificationId);
        notification.classList.add("show");

        setTimeout(() => {
            closeNotification(notificationId);
        }, 3000);
    }

    function closeNotification(notificationId) {
        const notification = document.getElementById(notificationId);
        notification.classList.remove("show");
    }
});

// Обработчик валидации для поля имени
document.addEventListener("DOMContentLoaded", function () {
    const FIOInput = document.getElementById('FIO');
    const errorMessage = FIOInput.nextElementSibling;

    FIOInput.addEventListener('blur', function () {
        const pattern = /^[А-Яа-яЁёA-Za-z\s]+$/;
        const value = FIOInput.value;

        if (!pattern.test(value)) {
            FIOInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'ФИО должно содержать только буквы и пробелы.';
        } else if (value.length < 2) {
            FIOInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'ФИО должно содержать не менее 2 символов.';
        } else if (value.length > 40) {
            FIOInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'ФИО должно содержать не более 40 символов.';
        } else {
            FIOInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });

    // Обработка отправки формы
    const form = document.getElementById('checkoutForm');
    form.addEventListener('submit', function (event) {
        const value = FIOInput.value;
        if (!FIOInput.validity.valid || value.length < 2 || value.length > 40) {
            errorMessage.style.display = 'inline';
            event.preventDefault();
        }
    });
});

// Обработчик валидации для поля email
document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById('email');
    const errorMessage = emailInput.nextElementSibling;

    emailInput.addEventListener('blur', function () {
        const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (!pattern.test(emailInput.value)) {
            emailInput.classList.add('error');
            errorMessage.style.display = 'inline';
        } else {
            emailInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });

    // Дополнительная проверка при отправке формы
    const form = document.getElementById('checkoutForm');
    form.addEventListener('submit', function (event) {
        if (!emailInput.validity.valid) {
            emailInput.classList.add('error');
            errorMessage.style.display = 'inline';
            event.preventDefault();
        }
    });
});

// Обработчик валидации для поля телефон
document.addEventListener("DOMContentLoaded", function () {
    const phoneInput = document.getElementById('phone');
    const errorMessage = phoneInput.nextElementSibling;

    phoneInput.addEventListener('blur', function () {
        const pattern = /^(\+7|8)\s?(\(?\d{3}\)?)[\s\-]?\d{3}[\s\-]?\d{2}[\s\-]?\d{2}$/;

        if (!pattern.test(phoneInput.value)) {
            phoneInput.classList.add('error');
            errorMessage.style.display = 'inline';
        } else {
            phoneInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });
});
