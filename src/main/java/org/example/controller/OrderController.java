package org.example.controller;

import org.example.model.Order;
import org.example.model.OrderDetails;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final Order order = new Order();

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getOrder(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("products", user.getOrder().getProducts());
        model.addAttribute("user", user);
        model.addAttribute("order", user.getOrder());
        return "order";
    }

    @PostMapping("/add/{productId}")
    public String addToOrder(@PathVariable Long productId, Principal principal) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        Product product = productService.getProductById(productId);
        User user = userService.getUserByPrincipal(principal);
        user.getOrder().getProducts().add(product);
        orderService.saveOrder(user.getOrder());
        return "redirect:/order";
    }

    @GetMapping("/orderdetails")
    public String getOrderDetails(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("order", user.getOrder());
        model.addAttribute("user", user);
        model.addAttribute("orderDetails", new OrderDetails());
        return "orderdetails";
    }

    @PostMapping("/orderdetails")
    public String processOrderDetails(@ModelAttribute("orderDetails") OrderDetails orderDetails, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        user.getOrder().getProducts().clear();
        orderService.saveOrder(user.getOrder());
        return "redirect:/order/orderplaced";
    }

    @GetMapping("/orderplaced")
    public String orderPlaced(Model model) {
        return "orderplaced";
    }

    @PostMapping("/delete/{productId}")
    public String deteleProduct(@PathVariable Long productId, Principal principal) {
        Product product = productService.getProductById(productId);
        User user = userService.getUserByPrincipal(principal);
        user.getOrder().getProducts().remove(product);
        orderService.saveOrder(user.getOrder());
        return "redirect:/order";
    }

}
