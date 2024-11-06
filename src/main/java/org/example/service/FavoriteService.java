package org.example.service;

import org.example.model.Favorite;
import org.example.model.FavoriteItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private ProductService productService;

    public void saveFavorite(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    public void deleteProduct(User user, Long productId) {
        List<FavoriteItem> products = user.getFavorite().getProducts();
        products.removeIf(item -> item.getProduct().getProductId().equals(productId));
        favoriteRepository.save(user.getFavorite());
    }

    public void saveIfIsNotAlreadyFavorite(User user, Long productId) {
        Product product = productService.getProductById(productId);
        boolean isAlreadyFavorite = user.getFavorite().getProducts()
                .stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));
        if (!isAlreadyFavorite) {
            FavoriteItem favoriteItem = new FavoriteItem(product.clone());
            user.getFavorite().getProducts().add(favoriteItem);
            favoriteRepository.save(user.getFavorite());
        }
    }

    public List<Product> getFavoriteProducts(User user) {
        return user.getFavorite().getProducts()
                .stream()
                .map(FavoriteItem::getProduct)
                .collect(Collectors.toList());
    }
}

