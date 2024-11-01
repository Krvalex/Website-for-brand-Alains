package org.example.controller;

import org.example.model.CartDetails;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.CartService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String getCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("products", user.getCart().getProducts());
        model.addAttribute("user", user);
        model.addAttribute("cart", user.getCart());
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Principal principal) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        Product product = productService.getProductById(productId);
        User user = userService.getUserByPrincipal(principal);
        user.getCart().getProducts().add(product);
        cartService.saveCart(user.getCart());
        return "redirect:/cart";
    }

    @GetMapping("/cartdetails")
    public String getCartDetails(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("cart", user.getCart());
        model.addAttribute("user", user);
        model.addAttribute("cartDetails", new CartDetails());
        return "cartdetails";
    }

    @PostMapping("/cartdetails")
    public String processCartDetails(@ModelAttribute("cartDetails") CartDetails cartDetails, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        user.getCart().getProducts().clear();
        cartService.saveCart(user.getCart());
        return "redirect:/cart/cartplaced";
    }

    @GetMapping("/cartplaced")
    public String cartPlaced(Model model) {
        return "cartplaced";
    }

    @PostMapping("/delete/{productId}")
    public String deteleProduct(@PathVariable Long productId, Principal principal) {
        Product product = productService.getProductById(productId);
        User user = userService.getUserByPrincipal(principal);
        user.getCart().getProducts().remove(product);
        cartService.saveCart(user.getCart());
        return "redirect:/cart";
    }

}
