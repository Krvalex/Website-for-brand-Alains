package org.alainshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.data.exception.InsufficientStockException;
import org.alainshop.model.CartItem;
import org.alainshop.model.GuestCart;
import org.alainshop.model.Product;
import org.alainshop.model.User;
import org.alainshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    ProductService productService;
    UserService userService;
    CartService cartService;
    OrderService orderService;
    CartItemService cartItemService;
    GuestCartService guestCartService;

    @GetMapping
    public String get(Principal principal, HttpSession session, Model model) {
        List<CartItem> cartItems;
        String formattedSum;
        int cartItemsCount;
        if (principal == null) { //если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getCartItems();
            formattedSum = cartItemService.formatSum(cartItems);
            cartItemsCount = guestCart.getCartItems().size();
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItemsCount = user.getCart().getCartItems().size();
            cartItems = user.getCart().getCartItems();
            formattedSum = cartItemService.formatSum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", formattedSum);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addProduct(@PathVariable Long productId,
                             @RequestParam String size,
                             Principal principal,
                             HttpSession session) {
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            Product product = productService.getById(productId);
            guestCartService.addItem(guestIdentifier, product, size, 1, session);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            Product product = productService.getById(productId);
            cartItemService.addToCart(user, product, size);
            cartService.save(user.getCart());
        }
        return "redirect:/products/" + productId;
    }

    @GetMapping("/details")
    public String getCartDetails(Model model, Principal principal, HttpSession session, RedirectAttributes redirectAttributes) {
        List<CartItem> cartItems;
        String sum;
        int cartItemsCount;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getCartItems();
            sum = cartItemService.formatSum(cartItems);
            cartItemsCount = guestCart.getCartItems().size();
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getCartItems();
            sum = cartItemService.formatSum(cartItems);
            cartItemsCount = user.getCart().getCartItems().size();
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ваша корзина пуста. Добавьте товары для оформления заказа.");
            return "redirect:/cart";
        }
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);
        return "cartDetails";
    }

    @PostMapping("/details")
    public String processCartDetails(@RequestParam("FIO") String FIO,
                                     @RequestParam("email") String email,
                                     @RequestParam("phone") String phone,
                                     @RequestParam("city") String city,
                                     @RequestParam("deliveryMethod") String deliveryMethod,
                                     @RequestParam("totalSum") double totalSum,
                                     Principal principal,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        List<CartItem> cartItems;
        try {
            if (principal == null) { // если гость
                String guestIdentifier = (String) session.getAttribute("guestIdentifier");
                GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
                cartItems = guestCart.getCartItems();
                cartItemService.decrementProducts(cartItems);
                orderService.createGuestOrder(cartItems, guestIdentifier, FIO, email, phone, city, deliveryMethod, totalSum);
            } else { // если пользователь
                User user = userService.getByPrincipal(principal);
                cartItems = user.getCart().getCartItems();
                cartItemService.decrementProducts(cartItems);
                orderService.createUserOrder(cartItems, user, FIO, email, phone, city, deliveryMethod, totalSum);
            }
        } catch (InsufficientStockException e) {
            redirectAttributes.addFlashAttribute("error", "Недостаточное количество товара на складе.");
            return "redirect:/cart";
        }
        model.addAttribute("cartItemsCount", 0);
        return "cartPlaced";
    }

    @PostMapping("/decrement/{cartItemId}")
    public String decrementProduct(@PathVariable Long cartItemId, Principal principal, HttpSession session) {
        if (principal == null) { // Если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestCartService.decrementProduct(guestIdentifier, cartItemId);
        } else { // Если пользователь
            User user = userService.getByPrincipal(principal);
            cartItemService.decrementProduct(user, cartItemId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/increment/{cartItemId}")
    public String incrementProduct(@PathVariable Long cartItemId, Principal principal, HttpSession session) {
        if (principal == null) { // Если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestCartService.incrementProduct(guestIdentifier, cartItemId);
        } else { // Если пользователь
            User user = userService.getByPrincipal(principal);
            cartItemService.incrementProduct(user, cartItemId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/delete/{cartItemId}")
    public String deleteProduct(@PathVariable Long cartItemId, Principal principal, HttpSession session) {
        if (principal == null) { // Если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            guestCartService.deleteProduct(guestIdentifier, cartItemId);
        } else { // Если пользователь
            User user = userService.getByPrincipal(principal);
            cartItemService.deleteProduct(user, cartItemId);
        }
        return "redirect:/cart";
    }

}
