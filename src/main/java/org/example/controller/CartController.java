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
    public String get(Principal principal, HttpSession session, Model model) {
        List<CartItem> cartItems;
        String formattedSum;
        if (principal == null) { //если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getCartItems();
            formattedSum = cartItemService.formatSum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getCartItems();
            formattedSum = cartItemService.formatSum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }

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

    @GetMapping("/cartdetails")
    public String getCartDetails(Model model, Principal principal, HttpSession session) {
        List<CartItem> cartItems;
        String sum;
        if (principal == null) { // если гость
            String guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getCartItems();
            sum = cartItemService.formatSum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getCartItems();
            sum = cartItemService.formatSum(cartItems);
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
        String sum;
        String address = cartDetails.getAddress();
        model.addAttribute("address", address);
        String guestIdentifier = null;

        if (principal == null) { // если гость
            guestIdentifier = (String) session.getAttribute("guestIdentifier");
            GuestCart guestCart = guestCartService.getOrCreate(guestIdentifier, session);
            cartItems = guestCart.getCartItems();
            //sum = cartItemService.formatSum(cartItems);
            model.addAttribute("cart", guestCart);
        } else { // если пользователь
            User user = userService.getByPrincipal(principal);
            cartItems = user.getCart().getCartItems();
            //sum = cartItemService.formatSum(cartItems);
            model.addAttribute("user", user);
            model.addAttribute("cart", user.getCart());
        }

        // Применение промокода
        if ("yes".equals(hasPromoCode) && "true".equals(applyPromoCode)) {
            PromoCode promo = promoCodeService.findByCode(promoCode);
            if (promo != null) {
//                double discount = sum * (promo.getDiscount() / 100);
//                sum -= discount;
//                model.addAttribute("discountMessage", "Скидка " + promo.getDiscount() + "% применена!");
            } else {
                model.addAttribute("errorMessage", "Недействительный промокод.");
            }
            model.addAttribute("cartItems", cartItems);
            //model.addAttribute("sum", sum);
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
                //model.addAttribute("sum", sum);
                return "cart";
            }
        }
        return "cart";
    }

    @GetMapping("/cartplaced")
    public String cartPlaced(Model model) {
        return "cartplaced";
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
