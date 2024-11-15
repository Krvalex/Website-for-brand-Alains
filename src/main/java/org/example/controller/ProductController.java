package org.example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.User;
import org.example.model.enums.Category;
import org.example.service.FavoriteService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    ProductService productService;
    UserService userService;
    FavoriteService favoriteService;

    @GetMapping
    public String getAll(Model model, @RequestParam(required = false) Category category, Principal principal) {
        List<Product> products = productService.getByCategory(category);
        List<Product> favoriteProducts = new ArrayList<>();
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            favoriteProducts = favoriteService.getProducts(user);
        }
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        model.addAttribute("favoriteProducts", favoriteProducts);
        return "products";
    }

    @GetMapping("/tshirts")
    public String getTshirts(Model model, Principal principal) {
        List<Product> products = productService.getByCategory(Category.T_SHIRTS);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        return "products";
    }

    @GetMapping("/hoodies")
    public String getHoodies(Model model, Principal principal) {
        List<Product> products = productService.getByCategory(Category.HOODIES);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        return "products";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Map<String, Integer> productSizes = product.getSizes(); // Получаем карту размеров и количества
        model.addAttribute("product", product);
        model.addAttribute("productSizes", productSizes);
        return "product-details";
    }
}