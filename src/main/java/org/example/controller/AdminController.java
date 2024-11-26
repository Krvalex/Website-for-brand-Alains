package org.example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
        return "adminUsersList";
    }

    @GetMapping("/create/product")
    public String addProduct() {
        return "adminAddProduct";
    }

    @PostMapping("/create/product")
    public String createProduct(@RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") double price,
                                @RequestParam("sizes") List<String> sizes,
                                @RequestParam("quantities") List<Integer> quantities,
                                @RequestParam("category") String category,
                                @RequestParam("imageFront") String imageFront,
                                @RequestParam("imageBack") String imageBack) {
        productService.create(name, description, price, sizes, quantities, category, imageFront, imageBack);
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String productList(Model model) {
        model.addAttribute("products", productService.getAll());
        return "adminProductsList";
    }

    @PostMapping("/delete/product")
    public String deleteProduct(@RequestParam Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/promo")
    public String promoList(Model model) {
        model.addAttribute("promoCodes", promoCodeService.getAll());
        return "adminPromoList";
    }

    @PostMapping("/add/promo")
    public String addPromo(@ModelAttribute PromoCode promoCode) {
        promoCodeService.add(promoCode);
        return "redirect:/admin/promo";
    }

    @PostMapping("/delete/promo/{id}")
    public String deletePromo(@PathVariable Long id) {
        promoCodeService.delete(id);
        return "redirect:/admin/promo";
    }
}

