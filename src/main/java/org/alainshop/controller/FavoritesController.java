package org.alainshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.FavoriteItem;
import org.alainshop.model.User;
import org.alainshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritesController {

    FavoritesService favoritesService;
    UserService userService;
    GuestFavoritesService guestFavoritesService;
    GuestCartService guestCartService;
    CartItemService cartItemService;

    @PostMapping("/{productId}")
    public String add(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            guestFavoritesService.saveIfNotAlreadyInFavorite(productId, session);
        } else { // если пользователь
            favoritesService.saveIfNotAlreadyInFavorite(userService.getByPrincipal(principal), productId);
        }
        return "redirect:/products";
    }

    @GetMapping
    public String get(Principal principal, HttpSession session, Model model) {
        List<FavoriteItem> favoriteItems;
        if (principal == null) { // если гость
            favoriteItems = guestFavoritesService.getOrCreate(session).getFavoriteItems();
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoriteItems = user.getFavorites().getFavoriteItems();
            model.addAttribute("user", user);
        }
        model.addAttribute("cartItemsCount", cartItemService.getCartItemsCount(principal, guestCartService.get(session)));
        model.addAttribute("favoriteItems", favoriteItems);
        return "favorites";
    }

    @PostMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            guestFavoritesService.deleteProductById(productId, session);
        } else { // если пользователь
            favoritesService.deleteProductById(userService.getByPrincipal(principal), productId);
        }
        return "redirect:/favorites";
    }

}