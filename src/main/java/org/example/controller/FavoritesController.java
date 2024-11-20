package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.FavoriteItem;
import org.example.model.GuestFavorites;
import org.example.model.User;
import org.example.service.FavoritesService;
import org.example.service.GuestFavoritesService;
import org.example.service.UserService;
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

    @PostMapping("/{productId}")
    public String add(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestFavoritesService.saveIfNotAlreadyInFavorite(guestIdentifier, productId, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoritesService.saveIfNotAlreadyInFavorite(user, productId);
        }
        return "redirect:/products";
    }

    @GetMapping
    public String get(Principal principal, HttpSession session, Model model) {
        List<FavoriteItem> favoriteItems;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestFavorites guestFavorites = guestFavoritesService.getOrCreate(guestIdentifier, session);
            favoriteItems = guestFavorites.getFavoriteItems();
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoriteItems = user.getFavorites().getFavoriteItems();
            model.addAttribute("user", user);
        }
        model.addAttribute("favoriteItems", favoriteItems);
        return "favorites";
    }

    @PostMapping("/remove/{favoriteItemId}")
    public String remove(@PathVariable Long favoriteItemId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestFavoritesService.deleteProduct(guestIdentifier, favoriteItemId, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoritesService.deleteProduct(user, favoriteItemId);
        }
        return "redirect:/favorites";
    }

}