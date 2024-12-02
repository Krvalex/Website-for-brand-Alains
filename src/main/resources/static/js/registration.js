// Обработчик валидации для поля имени
document.addEventListener("DOMContentLoaded", function () {
    const nameInput = document.getElementById('name');
    const errorMessage = nameInput.nextElementSibling;

    nameInput.addEventListener('blur', function () {
        const pattern = /^[А-Яа-яЁёA-Za-z\s]+$/;
        const value = nameInput.value;

        if (!pattern.test(value)) {
            nameInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Имя должно содержать только буквы и пробелы.';
        } else if (value.length < 2) {
            nameInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Имя должно содержать не менее 2 символов.';
        } else if (value.length > 40) {
            nameInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Имя должно содержать не более 30 символов.';
        } else {
            nameInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });

    // Обработка отправки формы
    const form = document.getElementById('registrationForm');
    form.addEventListener('submit', function (event) {
        const value = nameInput.value;
        if (!nameInput.validity.valid || value.length < 2 || value.length > 30) {
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
    const form = document.getElementById('registrationForm');
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
    const phoneInput = document.getElementById('phoneNumber');
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

// Обработчик валидации для поля пароль
document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById('password');
    const errorMessage = passwordInput.nextElementSibling;

    passwordInput.addEventListener('blur', function () {
        const value = passwordInput.value;

        if (value.length < 4) {
            passwordInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Пароль должен быть не менее 4 символов.';
        } else if (value.length > 15) {
            passwordInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Пароль не может быть более 15 символов.';
        } else if (!/^[a-zA-Z0-9!@#$%^&*()_+=-]*$/.test(value)) {
            passwordInput.classList.add('error');
            errorMessage.style.display = 'inline';
            errorMessage.textContent = 'Пароль может содержать только латинские буквы и специальные символы.';
        } else {
            passwordInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });

    // Обработка отправки формы
    const form = document.getElementById('registrationForm');
    form.addEventListener('submit', function (event) {
        if (!passwordInput.validity.valid) {
            errorMessage.style.display = 'inline';
            event.preventDefault();
        }
    });
});