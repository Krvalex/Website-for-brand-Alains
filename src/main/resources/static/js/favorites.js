function removeFavoriteItem(button) {
    const itemId = button.getAttribute('data-id');

    fetch(`/favorites/remove/${itemId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.redirected) {
                // Сервер вернул редирект, но мы его игнорируем и изменяем интерфейс вручную
                const favoriteItem = button.closest('.favorite-item');
                favoriteItem.remove();

                // Если список стал пустым, показываем сообщение
                const favoriteItemsContainer = document.querySelector('.favorite-items');
                const emptyMessage = document.querySelector('.empty-favorites-message');

                if (!favoriteItemsContainer.children.length) {
                    favoriteItemsContainer.style.display = "none";
                    emptyMessage.style.display = "block";
                }
            } else {
                console.error('Ошибка при удалении из избранного:', response.status);
            }
        })
        .catch(error => console.error('Ошибка сети:', error));
}
