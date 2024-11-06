package org.example.controller;

import lombok.RequiredArgsConstructor;
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
        User user = userService.getUserByPrincipal(principal);
        favoriteService.saveIfIsNotAlreadyFavorite(user, productId);
        return "redirect:/products/" + productId;
    }

    @GetMapping("/favorites")
    public String favoriteProducts(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        List<Product> products = favoriteService.getFavoriteProducts(user);
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