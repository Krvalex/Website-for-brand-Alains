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
        List<FavoriteItem> products = user.getFavorite().getProducts();
        products.removeIf(item -> item.getProduct().getProductId().equals(productId));
        favoriteRepository.save(user.getFavorite());
    }

    public void saveIfNotAlreadyInFavorite(User user, Long productId) {
        Product product = productService.getById(productId);
        boolean isAlreadyFavorite = user.getFavorite().getProducts()
                .stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));
        if (!isAlreadyFavorite) {
            FavoriteItem favoriteItem = new FavoriteItem(product.clone());
            user.getFavorite().getProducts().add(favoriteItem);
            favoriteRepository.save(user.getFavorite());
        }
    }

    public List<Product> getProducts(User user) {
        return user.getFavorite().getProducts()
                .stream()
                .map(FavoriteItem::getProduct)
                .collect(Collectors.toList());
    }
}

