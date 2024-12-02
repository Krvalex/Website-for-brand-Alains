package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.data.exception.InsufficientStockException;
import org.alainshop.model.CartItem;
import org.alainshop.model.GuestCart;
import org.alainshop.model.Product;
import org.alainshop.model.User;
import org.alainshop.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    CartItemRepository cartItemRepository;

    CartService cartService;
    UserService userService;

    public String formatSum(List<CartItem> items) {
        double sum = 0;
        for (CartItem item : items) {
            double price = Double.parseDouble(item.getProduct().getPrice().replace(" ", ""));
            sum += price * item.getQuantity();
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("ru", "RU"));
        return formatter.format(sum);
    }

    public void addToCart(User user, Product product, String size) {
        user.getCart().getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) && item.getSize().equals(size))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + 1), () -> {
                    CartItem cartItem = new CartItem(product.clone(), size, 1);
                    user.getCart().getCartItems().add(cartItem);
                    cartItemRepository.save(cartItem);
                });
    }

    public void deleteProduct(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();
        user.getCart().getCartItems().remove(cartItem);
        cartService.save(user.getCart());
    }

    public void decrementProduct(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            user.getCart().getCartItems().remove(cartItem);
        }
        cartService.save(user.getCart());
    }

    public void decrementProducts(List<CartItem> cartItems) throws InsufficientStockException {
        StringBuilder errorMessage = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            String size = cartItem.getSize();
            int requestedQuantity = cartItem.getQuantity();

            if (product.getSizes().containsKey(size)) {
                int availableQuantity = product.getSizes().get(size);
                if (availableQuantity >= requestedQuantity) {
                    product.getSizes().put(size, availableQuantity - requestedQuantity);
                } else {
                    errorMessage.append(String.format("Недостаточно товара %s размера %s. Осталось %d шт.\n",
                            product.getName(), size, availableQuantity));
                }
            } else {
                errorMessage.append(String.format("Товар %s размера %s отсутствует.\n", product.getName(), size));
            }
        }
        if (!errorMessage.isEmpty()) {
            throw new InsufficientStockException(errorMessage.toString());
        }
    }

    public void deleteByGuestCartId(Long guestCartId) {
        cartItemRepository.deleteByGuestCartId(guestCartId);
    }

    public void incrementProduct(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartService.save(user.getCart());
    }

    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public int getCartItemsCount(Principal principal, GuestCart guestCart) {
        int cartItemsCount = 0;
        if (principal != null) {
            cartItemsCount = userService.getByPrincipal(principal).getCart().getCartItems().size();
        } else {
            if (guestCart != null) {
                cartItemsCount = guestCart.getCartItems().size();
            }
        }
        return cartItemsCount;
    }
}
