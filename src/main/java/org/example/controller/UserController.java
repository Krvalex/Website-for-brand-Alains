package org.example.controller;

import org.example.model.Order;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderService productService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String lopginPost() {
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute User user, Model model) {
        try {
            userService.createUser(user);
            return "redirect:/users/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "registration"; // Перенаправление на страницу регистрации с сообщением об ошибке
        }
    }

    @PostMapping("/logout")
    public String logout(Principal principal) {
        userService.getUserByPrincipal(principal).setActive(false);
        return "redirect:/";
    }

    @GetMapping("/account")
    public String account(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        List<Order> orders = user.getOrders();
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "useraccount";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "userdetails";
    }
}
