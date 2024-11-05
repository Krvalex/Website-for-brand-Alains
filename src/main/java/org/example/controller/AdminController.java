package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Product;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/admin/users")
    public String usersList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "adminusers";
    }

    @GetMapping("/admin/create/product")
    public String addProduct() {
        return "/adminaddproduct";
    }

    @PostMapping("/admin/create/product")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("productSize") List<String> productSizes,
                                @RequestParam("productQuantity") List<Integer> productQuantities) {
        productService.saveNewProduct(product, productSizes, productQuantities);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/adminproducts";
    }

    @PostMapping("/admin/delete/product")
    public String deleteProductPost(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}

