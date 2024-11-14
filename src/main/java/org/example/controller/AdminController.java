package org.example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.PromoCode;
import org.example.service.ProductService;
import org.example.service.PromoCodeService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;
    ProductService productService;
    PromoCodeService promoCodeService;

    @GetMapping
    public String adminPanel() {
        return "/admin";
    }

    @GetMapping("/users")
    public String usersList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "adminusers";
    }

    @GetMapping("/create/product")
    public String addProduct() {
        return "/adminaddproduct";
    }

    @PostMapping("/create/product")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("productSize") List<String> productSizes,
                                @RequestParam("productQuantity") List<Integer> productQuantities) {
        productService.create(product, productSizes, productQuantities);
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "/adminproducts";
    }

    @PostMapping("/delete/product")
    public String deleteProduct(@RequestParam Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/promocodes")
    public String listPromocodes(Model model) {
        model.addAttribute("promocodes", promoCodeService.getAll());
        return "adminpromocodes";
    }

    @PostMapping("/add/promocode")
    public String addPromocode(@ModelAttribute PromoCode promoCode) {
        promoCodeService.add(promoCode);
        return "redirect:/admin/promocodes";
    }

    @PostMapping("/delete/promocode/{id}")
    public String deletePromocode(@PathVariable Long id) {
        promoCodeService.delete(id);
        return "redirect:/admin/promocodes";
    }
}

