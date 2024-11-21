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
