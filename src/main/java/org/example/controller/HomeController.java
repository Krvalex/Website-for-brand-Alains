package org.example.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.ProductService;
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
    ProductService productService;

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        User user = userService.getByPrincipal(principal);
        int cartItemsCount = user != null ? user.getCart().getCartItems().size() : 0;
        Product bestTshirt = productService.getProductByName("KINGVON T-SHIRT");
        Product bestHoodie = productService.getProductByName("ZIPALAINS HOODIE");
        model.addAttribute("user", user);
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("bestTshirt", bestTshirt);
        model.addAttribute("bestHoodie", bestHoodie);
        return "home";
    }

    @GetMapping("/garmentCare")
    public String garmentCare(Principal principal, Model model) {
        User user = userService.getByPrincipal(principal);
        model.addAttribute("user", user);
        return "garmentCare";
    }

    @GetMapping("/contacts")
    public String contacts(Principal principal, Model model) {
        User user = userService.getByPrincipal(principal);
        model.addAttribute("user", user);
        return "contacts";
    }
}
