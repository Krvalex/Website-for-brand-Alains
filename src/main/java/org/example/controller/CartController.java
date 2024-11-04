package org.example.controller;

import org.example.model.CartDetails;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.model.enums.Size;
import org.example.service.CartService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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

    @GetMapping
    public String getCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        List<Long> products_id = user.getCart().getProducts_id();
        List<Product> products = productService.getAllProductsById(products_id);
        double sum = productService.sum(products);
        model.addAttribute("products", products);
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
        Product productS = productService.findByProductNameAndProductSize(product.getProductName(), Size.valueOf(size));
        user.getCart().getProducts_id().add(productS.getProductId());
        cartService.saveCart(user.getCart());
        return "redirect:/products";
    }

    @GetMapping("/cartdetails")
    public String getCartDetails(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        List<Long> products_id = user.getCart().getProducts_id();
        List<Product> products = productService.getAllProductsById(products_id);
        double sum = productService.sum(products);
        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("cartDetails", new CartDetails());
        model.addAttribute("sum", sum);
        return "cartdetails";
    }

    @PostMapping("/cartdetails")
    public String processCartDetails(@ModelAttribute("cartDetails") CartDetails cartDetails, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        List<Long> products_id = user.getCart().getProducts_id();
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().withNano(0).withSecond(0));
        order.getProducts_id().addAll(products_id);
        order.setUser(user);
        List<Product> products = productService.getAllProductsById(products_id);
        productService.decrementProduct(products);
        user.getCart().getProducts_id().clear();
        cartService.saveCart(user.getCart());
        orderService.saveOrder(order);
        return "redirect:/cart/cartplaced";
    }

    @GetMapping("/cartplaced")
    public String cartPlaced(Model model) {
        return "cartplaced";
    }

    @PostMapping("/delete/{productId}")
    public String deteleProduct(@PathVariable Long productId, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        user.getCart().getProducts_id().remove(productId);
        cartService.saveCart(user.getCart());
        return "redirect:/cart";
    }

}
