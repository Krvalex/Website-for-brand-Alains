package org.example.service;

import org.example.model.Cart;
import org.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
