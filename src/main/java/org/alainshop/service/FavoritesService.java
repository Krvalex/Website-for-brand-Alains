package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.FavoriteItem;
import org.alainshop.model.Product;
import org.alainshop.model.User;
import org.alainshop.model.enums.Category;
import org.alainshop.repository.FavoritesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class FavoritesService {

    FavoritesRepository favoritesRepository;
    ProductService productService;

    public void deleteProductById(User user, Long favoriteItemId) {
        List<FavoriteItem> favoriteItems = user.getFavorites().getFavoriteItems();
        favoriteItems.removeIf(favoriteItem -> favoriteItem.getProduct().getId().equals(favoriteItemId));
        favoritesRepository.save(user.getFavorites());
    }

    public void saveIfNotAlreadyInFavorite(User user, Long productId) {
        Product product = productService.getById(productId);
        boolean isAlreadyFavorite = user.getFavorites().getFavoriteItems()
                .stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));
        if (!isAlreadyFavorite) {
            FavoriteItem favoriteItem = new FavoriteItem(product.clone());
            user.getFavorites().getFavoriteItems().add(favoriteItem);
            favoritesRepository.save(user.getFavorites());
        }
    }

    public List<Product> getProducts(User user) {
        return user.getFavorites().getFavoriteItems()
                .stream()
                .map(FavoriteItem::getProduct)
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(User user, Category category) {
        return user.getFavorites().getFavoriteItems()
                .stream()
                .map(FavoriteItem::getProduct)
                .filter(product -> product.getCategory() == category) // Фильтрация по категории
                .collect(Collectors.toList());
    }

    public boolean isFavorite(User user, Product product) {
        return user.getFavorites().getFavoriteItems()
                .stream()
                .anyMatch(favoriteItem -> favoriteItem.getProduct().getId().equals(product.getId()));
    }
}

