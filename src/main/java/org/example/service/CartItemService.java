package org.example.service;

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
    @Autowired
    private ProductService productService;

    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }


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

    public void decrementProducts(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            String size = cartItem.getSize();
            int quantity = cartItem.getQuantity();

            if (product.getProductSizes().containsKey(size)) {
                int currentQuantity = product.getProductSizes().get(size);
                if (currentQuantity >= quantity) {
                    product.getProductSizes().put(size, currentQuantity - quantity);
                } else {
                    System.out.println("Недостаточно товара в наличии.");
                }
            }
        }
    }
}
