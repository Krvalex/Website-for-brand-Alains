package org.alainshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.FavoriteItem;
import org.alainshop.model.GuestCart;
import org.alainshop.model.GuestFavorites;
import org.alainshop.model.User;
import org.alainshop.service.FavoritesService;
import org.alainshop.service.GuestCartService;
import org.alainshop.service.GuestFavoritesService;
import org.alainshop.service.UserService;
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
        int cartItemsCount = 0;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestFavorites guestFavorites = guestFavoritesService.getOrCreate(guestIdentifier, session);
            favoriteItems = guestFavorites.getFavoriteItems();
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoriteItems = user.getFavorites().getFavoriteItems();
            cartItemsCount = user.getCart().getCartItems().size();
            model.addAttribute("user", user);
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("favoriteItems", favoriteItems);
        return "favorites";
    }

    @PostMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId, Principal principal, HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            System.out.println("Post remove");
            guestFavoritesService.deleteProductById(guestIdentifier, productId);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            favoritesService.deleteProductById(user, productId);
        }
        return "redirect:/favorites";
    }

}