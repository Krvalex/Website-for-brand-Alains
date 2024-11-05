package org.example.controller;

import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllProducts(Model model, @RequestParam(required = false) Category category, Principal principal) {
        List<Product> products = productService.getProducts(category);
        model.addAttribute("products", products);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("categories", Category.values());
        return "products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<String> sizes = productService.getSizesFromProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("sizes", sizes);
        return "product-details";
    }
}