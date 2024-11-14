package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.FavoriteItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class FavoriteService {

    FavoriteRepository favoriteRepository;
    ProductService productService;

    public void deleteProduct(User user, Long productId) {
        List<FavoriteItem> products = user.getFavorite().getFavoriteItems();
        products.removeIf(item -> item.getProduct().getId().equals(productId));
        favoriteRepository.save(user.getFavorite());
    }

    public void saveIfNotAlreadyInFavorite(User user, Long productId) {
        Product product = productService.getById(productId);
        boolean isAlreadyFavorite = user.getFavorite().getFavoriteItems()
                .stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));
        if (!isAlreadyFavorite) {
            FavoriteItem favoriteItem = new FavoriteItem(product.clone());
            user.getFavorite().getFavoriteItems().add(favoriteItem);
            favoriteRepository.save(user.getFavorite());
        }
    }

    public List<Product> getProducts(User user) {
        return user.getFavorite().getFavoriteItems()
                .stream()
                .map(FavoriteItem::getProduct)
                .collect(Collectors.toList());
    }
}

