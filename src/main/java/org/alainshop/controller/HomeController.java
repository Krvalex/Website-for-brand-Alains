package org.alainshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.Product;
import org.alainshop.service.CartItemService;
import org.alainshop.service.GuestCartService;
import org.alainshop.service.ProductService;
import org.alainshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
public class HomeController {

    UserService userService;
    ProductService productService;
    GuestCartService guestCartService;
    CartItemService cartItemService;

    @GetMapping("/")
    public String home(Principal principal, Model model, HttpSession session) {
        int cartItemsCount = cartItemService.getCartItemsCount(principal, guestCartService.get(session));
        model.addAttribute("user", userService.getByPrincipal(principal));
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("bestTshirt", productService.getProductByName("KINGVON T-SHIRT"));
        model.addAttribute("bestHoodie", productService.getProductByName("ZIPALAINS HOODIE"));
        return "home";
    }

    @GetMapping("/garmentCare")
    public String garmentCare(Principal principal, Model model, HttpSession session) {
        int cartItemsCount = cartItemService.getCartItemsCount(principal, guestCartService.get(session));
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("user", userService.getByPrincipal(principal));
        return "garmentCare";
    }

    @GetMapping("/contacts")
    public String contacts(Principal principal, Model model, HttpSession session) {
        int cartItemsCount = cartItemService.getCartItemsCount(principal, guestCartService.get(session));
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("user", userService.getByPrincipal(principal));
        return "contacts";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchByName(query);
        model.addAttribute("products", products);
        return "searchResults";
    }
}
