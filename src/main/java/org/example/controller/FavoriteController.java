package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.FavoriteService;
import org.example.service.GuestFavoriteService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequiredArgsConstructor
public class FavoriteController {

    @Autowired
    final FavoriteService favoriteService;
    @Autowired
    UserService userService;
    @Autowired
    GuestFavoriteService guestFavoriteService;

    @PostMapping("/favorites/{productId}")
    public String favoriteProduct(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestFavoriteService.saveIfNotAlreadyInFavorite(guestIdentifier, productId, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoriteService.saveIfNotAlreadyInFavorite(user, productId);
        }
        return "redirect:/products/" + productId;
    }

    @GetMapping("/favorites")
    public String favoriteProducts(Principal principal, HttpSession session, Model model) {
        List<Product> products;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            products = guestFavoriteService.getProducts(guestIdentifier, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            products = favoriteService.getProducts(user);
            model.addAttribute("user", user);
        }
        model.addAttribute("products", products);
        return "favorites";
    }

    @PostMapping("/favorites/remove/{productId}")
    public String removeFavoriteProduct(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestFavoriteService.deleteProduct(guestIdentifier, productId, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoriteService.deleteProduct(user, productId);
        }
        return "redirect:/favorites";
    }

}