package org.alainshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.User;
import org.alainshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost() {
        return "userAccount";
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
        return "home";
    }

    @GetMapping("/account")
    public String account(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.getByPrincipal(principal);
            model.addAttribute("cartItemsCount", user.getCart().getCartItems().size());
            model.addAttribute("user", user);
            model.addAttribute("orders", user.getOrders());
            return "userAccount";
        }
        return "login";
    }
}
