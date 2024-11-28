package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.Cart;
import org.alainshop.model.User;
import org.alainshop.repository.CartRepository;
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
        user.getCart().getCartItems().clear();
    }
}
