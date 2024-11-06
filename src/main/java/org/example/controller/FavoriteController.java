package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.FavoriteItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.FavoriteService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    @Autowired
    private final FavoriteService favoriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping("/favorites/{productId}")
    public String favoriteProduct(@PathVariable Long productId, Principal principal) {
        Product product = productService.getProductById(productId);
        User user = userService.getUserByPrincipal(principal);
        boolean isAlreadyFavorite = user.getFavorite().getProducts()
                .stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));
        if (!isAlreadyFavorite) {
            // Создаем FavoriteItem и добавляем его в избранное
            FavoriteItem favoriteItem = new FavoriteItem(product.clone());
            user.getFavorite().getProducts().add(favoriteItem);
            favoriteService.saveFavorite(user.getFavorite());
        }
        return "redirect:/products/" + productId;
    }

    @GetMapping("/favorites")
    public String favoriteProducts(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        List<Product> products = user.getFavorite().getProducts()
                .stream()
                .map(FavoriteItem::getProduct)
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("products", products);
        return "favorites";
    }

    @PostMapping("/favorites/remove/{productId}")
    public String removeFavoriteProduct(@PathVariable Long productId, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        favoriteService.deleteProduct(user, productId);
        return "redirect:/favorites";
    }

}