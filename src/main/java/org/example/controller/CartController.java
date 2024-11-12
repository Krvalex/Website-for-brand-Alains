package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.data.exception.InsufficientStockException;
import org.example.model.*;
import org.example.service.*;
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
    PromoCodeService promoCodeService;
    GuestCartService guestCartService;

    @GetMapping
    public String getCart(Principal principal, HttpSession session, Model model) {
        List<CartItem> cartItems;
        double sum;
        if (principal == null) { //если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam String size,
                            Principal principal,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (size == null || size.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите размер.");
            return "redirect:/products/" + productId;
        }

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
        return "redirect:/cart";
    }

    @GetMapping("/cartdetails")
    public String getCartDetails(Model model, Principal principal, HttpSession session) {
        List<CartItem> cartItems;
        double sum;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartDetails", new CartDetails());
        model.addAttribute("sum", sum);
        return "cartdetails";
    }

    @PostMapping("/cartdetails")
    public String processCartDetails(@ModelAttribute("cartDetails") CartDetails cartDetails,
                                     @RequestParam(required = false) String promoCode,
                                     @RequestParam String hasPromoCode,
                                     @RequestParam(required = false) String applyPromoCode,
                                     Principal principal,
                                     HttpSession session,
                                     Model model) {

        List<CartItem> cartItems;
        double sum;
        String address = cartDetails.getAddress();
        model.addAttribute("address", address);
        String guestIdentifier = null;

        if (principal == null) { // если гость
            guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getProducts();
            sum = cartItemService.sum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }

        // Применение промокода
        if ("yes".equals(hasPromoCode) && "true".equals(applyPromoCode)) {
            PromoCode promo = promoCodeService.findByCode(promoCode);
            if (promo != null) {
                double discount = sum * (promo.getDiscountPercentage() / 100);
                sum -= discount;
                model.addAttribute("discountMessage", "Скидка " + promo.getDiscountPercentage() + "% применена!");
            } else {
                model.addAttribute("errorMessage", "Недействительный промокод.");
            }
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("sum", sum);
            return "cartdetails";
        }

        // Оформление заказа
        if ("yes".equals(hasPromoCode) && "false".equals(applyPromoCode) || "no".equals(hasPromoCode)) {
            try {
                cartItemService.decrementProducts(cartItems);
                if (principal == null) { // если гость
                    orderService.createGuestOrder(cartItems, address, guestIdentifier);
                } else { // если пользователь
                    User user = userService.getByPrincipal(principal);
                    orderService.createUserOrder(cartItems, user);
                }
                return "cartplaced";
            } catch (InsufficientStockException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("sum", sum);
                return "cart";
            }
        }
        return "cart";
    }

    @GetMapping("/cartplaced")
    public String cartPlaced(Model model) {
        return "cartplaced";
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
