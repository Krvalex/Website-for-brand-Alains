package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
            userService.create(user);
            return "redirect:/users/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "registration";
        }
    }

    @PostMapping("/logout")
    public String logout(Principal principal) {
        userService.getByPrincipal(principal).setActive(false);
        return "redirect:/";
    }

    @GetMapping("/account")
    public String account(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            model.addAttribute("user", user);
            model.addAttribute("orders", user.getOrders());
            return "useraccount";
        }
        return "login";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "userdetails";
    }
}
