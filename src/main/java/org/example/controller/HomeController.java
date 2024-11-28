package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.GuestCart;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.GuestCartService;
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
    GuestCartService guestCartService;

    @GetMapping("/")
    public String home(Principal principal, Model model, HttpSession session) {
        User user = userService.getByPrincipal(principal);
        int cartItemsCount = 0;
        if (user != null) {
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        Product bestTshirt = productService.getProductByName("KINGVON T-SHIRT");
        Product bestHoodie = productService.getProductByName("ZIPALAINS HOODIE");
        model.addAttribute("user", user);
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("bestTshirt", bestTshirt);
        model.addAttribute("bestHoodie", bestHoodie);
        return "home";
    }

    @GetMapping("/garmentCare")
    public String garmentCare(Principal principal, Model model, HttpSession session) {
        User user = userService.getByPrincipal(principal);
        int cartItemsCount = 0;
        if (user != null) {
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("user", user);
        return "garmentCare";
    }

    @GetMapping("/contacts")
    public String contacts(Principal principal, Model model, HttpSession session) {
        User user = userService.getByPrincipal(principal);
        int cartItemsCount = 0;
        if (user != null) {
            cartItemsCount = user.getCart().getCartItems().size();
        } else {
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.get(guestIdentifier);
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("user", user);
        return "contacts";
    }
}
