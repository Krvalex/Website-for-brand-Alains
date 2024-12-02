package org.alainshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.Product;
import org.alainshop.model.enums.Category;
import org.alainshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

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
    CartItemService cartItemService;

    @GetMapping
    public String getAll(Model model, @RequestParam(required = false) Category category, Principal principal, HttpSession session) {
        List<Product> products = productService.getByCategory(category);
        List<Product> favoriteProducts;
        if (principal != null) {
            favoriteProducts = favoritesService.getProducts(userService.getByPrincipal(principal));
        } else {
            favoriteProducts = guestFavoritesService.getProducts(session);
        }
        model.addAttribute("cartItemsCount", cartItemService.getCartItemsCount(principal, guestCartService.get(session)));
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
        if (principal != null) {
            favoriteProducts = favoritesService.getProductsByCategory(userService.getByPrincipal(principal), Category.T_SHIRTS);
        } else {
            favoriteProducts = guestFavoritesService.getProductsByCategory(session, Category.T_SHIRTS);
        }
        model.addAttribute("cartItemsCount", cartItemService.getCartItemsCount(principal, guestCartService.get(session)));
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
        if (principal != null) {
            favoriteProducts = favoritesService.getProductsByCategory(userService.getByPrincipal(principal), Category.HOODIES);
        } else {
            favoriteProducts = guestFavoritesService.getProductsByCategory(session, Category.HOODIES);
        }
        model.addAttribute("cartItemsCount", cartItemService.getCartItemsCount(principal, guestCartService.get(session)));
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
        boolean isFavoriteProduct;
        if (principal != null) {
            isFavoriteProduct = favoritesService.isFavorite(userService.getByPrincipal(principal), product);
        } else {
            isFavoriteProduct = guestFavoritesService.isFavorite(session, product);
        }
        model.addAttribute("isFavoriteProduct", isFavoriteProduct);
        model.addAttribute("cartItemsCount", cartItemService.getCartItemsCount(principal, guestCartService.get(session)));
        model.addAttribute("product", product);
        model.addAttribute("productSizes", product.getSizes());
        model.addAttribute("oldPrice", productService.getOldPrice(product.getPrice()));
        return "productDetails";
    }
}