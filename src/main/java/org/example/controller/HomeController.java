package org.example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
public class HomeController {

    UserService userService;

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        User user = userService.getByPrincipal(principal);
        int cartItemsCount = user != null ? user.getCart().getCartItems().size() : 0;
        model.addAttribute("user", user);
        model.addAttribute("cartItemsCount", cartItemsCount);
        return "home";
    }
}
