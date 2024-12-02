package org.alainshop.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.CartItem;
import org.alainshop.model.GuestCart;
import org.alainshop.model.Product;
import org.alainshop.repository.GuestCartRepository;
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
    public GuestCart getOrCreate(HttpSession session) {
        String guestIdentifier = (String) session.getAttribute("guestIdentifier");
        if (guestIdentifier == null) {
            guestIdentifier = UUID.randomUUID().toString();
            session.setAttribute("guestIdentifier", guestIdentifier);
        }
        final String identifier = guestIdentifier;
        return guestCartRepository.findByGuestIdentifier(identifier)
                .orElseGet(() -> create(identifier));
    }

    public GuestCart get(HttpSession session) {
        String guestIdentifier = (String) session.getAttribute("guestIdentifier");
        return guestCartRepository.findByGuestIdentifier(guestIdentifier)
                .orElse(null);
    }

    private GuestCart create(String guestIdentifier) {
        GuestCart newCart = new GuestCart(guestIdentifier, LocalDateTime.now());
        return guestCartRepository.save(newCart);
    }

    @Transactional
    public void addToCart(Product product, String size, HttpSession session) {
        GuestCart guestCart = getOrCreate(session);
        guestCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) && item.getSize().equals(size))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + 1), () -> {
                    CartItem cartItem = new CartItem(product.clone(), size, 1);
                    guestCart.getCartItems().add(cartItem);
                    cartItemService.save(cartItem);
                });
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