package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Cart;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CartService {

    CartRepository cartRepository;

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public void clear(User user) {
        user.getCart().getProducts().clear();
    }
}
