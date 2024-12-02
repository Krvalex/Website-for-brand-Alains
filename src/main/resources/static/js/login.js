// Обработчик валидации для поля username
document.addEventListener("DOMContentLoaded", function () {
    const usernameInput = document.getElementById('username');
    const errorMessage = usernameInput.nextElementSibling;

    usernameInput.addEventListener('blur', function () {
        const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (!pattern.test(usernameInput.value)) {
            usernameInput.classList.add('error');
            errorMessage.style.display = 'inline';
        } else {
            usernameInput.classList.remove('error');
            errorMessage.style.display = 'none';
        }
    });

    // Дополнительная проверка при отправке формы
    const form = document.getElementById('loginForm');
    form.addEventListener('submit', function (event) {
        if (!usernameInput.validity.valid) {
            usernameInput.classList.add('error');
            errorMessage.style.display = 'inline';
            event.preventDefault();
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
    const form = document.getElementById('loginForm');
    form.addEventListener('submit', function (event) {
        if (!passwordInput.validity.valid) {
            errorMessage.style.display = 'inline';
            event.preventDefault();
        }
    });
});