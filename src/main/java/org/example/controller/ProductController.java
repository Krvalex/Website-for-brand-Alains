package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.GuestCart;
import org.example.model.Product;
import org.example.model.User;
import org.example.model.enums.Category;
import org.example.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    ProductService productService;
    UserService userService;
    FavoritesService favoritesService;
    GuestFavoritesService guestFavoritesService;
    GuestCartService guestCartService;

    @GetMapping
    public String getAll(Model model, @RequestParam(required = false) Category category, Principal principal, HttpSession session) {
        List<Product> products = productService.getByCategory(category);
        List<Product> favoriteProducts;
        int cartItemsCount = 0;
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            favoriteProducts = favoritesService.getProducts(user);
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            favoriteProducts = guestFavoritesService.getProducts(guestIdentifier);
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        model.addAttribute("favoriteProducts", favoriteProducts);
        model.addAttribute("category", category != null ? category.name() : "Все товары");
        return "products";
    }

    @GetMapping("/tshirts")
    public String getTshirts(Model model, Principal principal, HttpSession session) {
        List<Product> products = productService.getByCategory(Category.T_SHIRTS);
        List<Product> favoriteProducts;
        int cartItemsCount = 0;
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            favoriteProducts = favoritesService.getProductsByCategory(user, Category.T_SHIRTS);
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            favoriteProducts = guestFavoritesService.getProductsByCategory(guestIdentifier, Category.T_SHIRTS);
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        model.addAttribute("favoriteProducts", favoriteProducts);
        model.addAttribute("category", "Футболки");
        return "products";
    }

    @GetMapping("/hoodies")
    public String getHoodies(Model model, Principal principal, HttpSession session) {
        List<Product> products = productService.getByCategory(Category.HOODIES);
        List<Product> favoriteProducts;
        int cartItemsCount = 0;
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            favoriteProducts = favoritesService.getProductsByCategory(user, Category.HOODIES);
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            favoriteProducts = guestFavoritesService.getProductsByCategory(guestIdentifier, Category.HOODIES);
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        model.addAttribute("favoriteProducts", favoriteProducts);
        model.addAttribute("category", "Худи");
        return "products";
    }


    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model, Principal principal, HttpSession session) {
        Product product = productService.getById(id);
        Map<String, Integer> productSizes = product.getSizes(); // Получаем карту размеров и количества
        String oldPrice = productService.getOldPrice(product.getPrice());
        int cartItemsCount = 0;
        if (principal != null) {
            cartItemsCount = userService.getByPrincipal(principal).getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("product", product);
        model.addAttribute("productSizes", productSizes);
        model.addAttribute("oldPrice", oldPrice);
        return "productDetails";
    }
}