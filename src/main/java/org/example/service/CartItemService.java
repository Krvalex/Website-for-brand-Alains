package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.data.exception.InsufficientStockException;
import org.example.model.CartItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    CartItemRepository cartItemRepository;

    CartService cartService;

    public double sum(List<CartItem> items) {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getProduct().getPrice();
        }
        return sum;
    }

    public void addToCart(User user, Product product, String size) {
        CartItem existingCartItem = user.getCart().getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) && item.getSize().equals(size))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem(product.clone(), size, 1);
            user.getCart().getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }
    }

    public void deleteProduct(User user, Long cartItemId) {
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
                    errorMessage.append("Недостаточно товара ")
                            .append(product.getName())
                            .append(" размера ")
                            .append(size)
                            .append(". Осталось ")
                            .append(availableQuantity)
                            .append(" шт.\n");
                }
            } else {
                errorMessage.append("Товар ")
                        .append(product.getName())
                        .append(" размера ")
                        .append(size)
                        .append(" отсутствует.\n");
            }
        }
        if (!errorMessage.isEmpty()) {
            throw new InsufficientStockException(errorMessage.toString());
        }
    }

    public void deleteByGuestCartId(Long guestCartId) {
        cartItemRepository.deleteByGuestCartId(guestCartId);
    }
}
