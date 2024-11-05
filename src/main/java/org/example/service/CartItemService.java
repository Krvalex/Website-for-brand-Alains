package org.example.service;

import org.example.handler.InsufficientStockException;
import org.example.model.CartItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public double sum(List<CartItem> items) {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getProduct().getProductPrice();
        }
        return sum;
    }

    public void addToCart(User user, Product product, String size) {
        CartItem existingCartItem = user.getCart().getProducts().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()) && item.getSize().equals(size))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem(product.clone(), size, 1);
            user.getCart().getProducts().add(cartItem);
            cartItemRepository.save(cartItem);
        }
    }

    public void deleteProduct(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            user.getCart().getProducts().remove(cartItem);
        }
    }

    public void decrementProducts(List<CartItem> cartItems) throws InsufficientStockException {
        StringBuilder errorMessage = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            String size = cartItem.getSize();
            int requestedQuantity = cartItem.getQuantity();

            if (product.getProductSizes().containsKey(size)) {
                int availableQuantity = product.getProductSizes().get(size);
                if (availableQuantity >= requestedQuantity) {
                    product.getProductSizes().put(size, availableQuantity - requestedQuantity);
                } else {
                    errorMessage.append("Недостаточно товара ")
                            .append(product.getProductName())
                            .append(" размера ")
                            .append(size)
                            .append(". Осталось ")
                            .append(availableQuantity)
                            .append(" шт.\n");
                }
            } else {
                errorMessage.append("Товар ")
                        .append(product.getProductName())
                        .append(" размера ")
                        .append(size)
                        .append(" отсутствует.\n");
            }
        }
        if (errorMessage.length() > 0) {
            throw new InsufficientStockException(errorMessage.toString());
        }
    }
}
