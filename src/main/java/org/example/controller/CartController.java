package org.example.controller;

import org.example.handler.InsufficientStockException;
import org.example.model.CartDetails;
import org.example.model.CartItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public String getCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        List<CartItem> cartItems = user.getCart().getProducts();
        double sum = cartItemService.sum(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("cart", user.getCart());
        model.addAttribute("sum", sum);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam String size, Principal principal) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        Product product = productService.getProductById(productId);
        cartItemService.addToCart(user, product, size);
        cartService.saveCart(user.getCart());
        return "redirect:/cart";
    }

    @GetMapping("/cartdetails")
    public String getCartDetails(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        List<CartItem> cartItems = user.getCart().getProducts();
        double sum = cartItemService.sum(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("cartDetails", new CartDetails());
        model.addAttribute("sum", sum);
        return "cartdetails";
    }

    @PostMapping("/cartdetails")
    public String processCartDetails(@ModelAttribute("cartDetails") CartDetails cartDetails, Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        List<CartItem> cartItems = user.getCart().getProducts();
        try {
            orderService.createNewOrder(cartItems, user);
            cartItemService.decrementProducts(cartItems);
            userService.clearCart(user);
            cartService.saveCart(user.getCart());
        } catch (InsufficientStockException e) {
            double sum = cartItemService.sum(cartItems);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
            model.addAttribute("sum", sum);
            return "cart";
        }
        return "redirect:/cart/cartplaced";
    }

    @GetMapping("/cartplaced")
    public String cartPlaced(Model model) {
        return "cartplaced";
    }

    @PostMapping("/delete/{cartItemId}")
    public String deteleProduct(@PathVariable Long cartItemId, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        cartItemService.deleteProduct(user, cartItemId);
        cartService.saveCart(user.getCart());
        return "redirect:/cart";
    }

}
