package org.example.service;

import org.example.model.Favorite;
import org.example.model.FavoriteItem;
import org.example.model.User;
import org.example.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public void saveFavorite(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    public void deleteProduct(User user, Long productId) {
        List<FavoriteItem> products = user.getFavorite().getProducts();

        // Удаление FavoriteItem по productId
        products.removeIf(item -> item.getProduct().getProductId().equals(productId));

        favoriteRepository.save(user.getFavorite());
    }
}

