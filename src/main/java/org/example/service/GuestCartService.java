package org.example.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.CartItem;
import org.example.model.GuestCart;
import org.example.model.Product;
import org.example.repository.GuestCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class GuestCartService {

    GuestCartRepository guestCartRepository;

    CartItemService cartItemService;

    @Transactional
    public GuestCart getOrCreate(String guestIdentifier, HttpSession session) {
        if (guestIdentifier == null) {
            guestIdentifier = UUID.randomUUID().toString();
            session.setAttribute("guestIdentifier", guestIdentifier);
        }
        final String identifier = guestIdentifier;
        return guestCartRepository.findByGuestIdentifier(identifier)
                .orElseGet(() -> create(identifier));
    }

    private GuestCart create(String guestIdentifier) {
        GuestCart newCart = new GuestCart(guestIdentifier, LocalDateTime.now());
        return guestCartRepository.save(newCart);
    }

    @Transactional
    public void addItem(String guestIdentifier, Product product, String size, int quantity, HttpSession session) {
        GuestCart guestCart = getOrCreate(guestIdentifier, session);
        CartItem cartItem = new CartItem(product, size, quantity);
        guestCart.getCartItems().add(cartItem);
        guestCartRepository.save(guestCart);
    }

    public void clear(String guestIdentifier) {
        GuestCart guestCart = guestCartRepository.findByGuestIdentifier(guestIdentifier).orElseThrow();
        guestCart.getCartItems().clear();
        guestCartRepository.save(guestCart);
    }

    public void deleteProduct(String guestIdentifier, Long cartItemId) {
        GuestCart guestCart = guestCartRepository.findByGuestIdentifier(guestIdentifier).orElseThrow();
        guestCart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        guestCartRepository.save(guestCart);
    }

    @Transactional
    public void cleanUpIfOld(LocalDateTime expirationTime) {
        List<GuestCart> oldCarts = guestCartRepository.findOldGuestCarts(expirationTime);
        for (GuestCart oldCart : oldCarts) {
            cartItemService.deleteByGuestCartId(oldCart.getId());
        }
        guestCartRepository.deleteAll(oldCarts);
    }

    public void incrementProduct(String guestIdentifier, Long cartItemId) {
        GuestCart guestCart = guestCartRepository.findByGuestIdentifier(guestIdentifier).orElseThrow();
        for (CartItem cartItem : guestCart.getCartItems()) {
            if (cartItem.getId().equals(cartItemId)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                break;
            }
        }
        guestCartRepository.save(guestCart);
    }

    public void decrementProduct(String guestIdentifier, Long cartItemId) {
        GuestCart guestCart = guestCartRepository.findByGuestIdentifier(guestIdentifier).orElseThrow();
        for (CartItem cartItem : guestCart.getCartItems()) {
            if (cartItem.getId().equals(cartItemId)) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                break;
            }
        }
        guestCartRepository.save(guestCart);
    }
}