package org.example.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.FavoriteItem;
import org.example.model.GuestFavorite;
import org.example.model.Product;
import org.example.repository.GuestFavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class GuestFavoriteService {

    GuestFavoriteRepository guestFavoriteRepository;

    FavoriteItemService favoriteItemService;
    ProductService productService;

    public GuestFavorite getOrCreate(String guestIdentifier, HttpSession session) {
        if (guestIdentifier == null) {
            guestIdentifier = UUID.randomUUID().toString();
            session.setAttribute("guestIdentifier", guestIdentifier);
        }
        final String identifier = guestIdentifier;
        return guestFavoriteRepository.findByGuestIdentifier(identifier)
                .orElseGet(() -> create(identifier));
    }

    public void saveIfNotAlreadyInFavorite(String guestIdentifier, Long productId, HttpSession session) {
        GuestFavorite guestFavorite = getOrCreate(guestIdentifier, session);
        if (guestFavorite.getFavoriteItems().stream().noneMatch(f -> f.getProduct().getId().equals(productId))) {
            Product product = productService.getById(productId);
            guestFavorite.getFavoriteItems().add(new FavoriteItem(product));
            guestFavoriteRepository.save(guestFavorite);
        }
    }

    public List<Product> getProducts(String guestIdentifier, HttpSession session) {
        GuestFavorite guestFavorite = getOrCreate(guestIdentifier, session);
        return guestFavorite.getFavoriteItems().stream()
                .map(FavoriteItem::getProduct)
                .toList();
    }

    public void deleteProduct(String guestIdentifier, Long productId, HttpSession session) {
        GuestFavorite guestFavorite = getOrCreate(guestIdentifier, session);
        guestFavorite.getFavoriteItems().removeIf(f -> f.getProduct().getId().equals(productId));
        guestFavoriteRepository.save(guestFavorite);
    }

    private GuestFavorite create(String guestIdentifier) {
        GuestFavorite newFavorite = new GuestFavorite(guestIdentifier, LocalDateTime.now());
        return guestFavoriteRepository.save(newFavorite);
    }

    @Transactional
    public void cleanUpIfOld(LocalDateTime expirationTime) {
        List<GuestFavorite> oldFavorites = guestFavoriteRepository.findOldGuestFavorites(expirationTime);
        for (GuestFavorite oldFavorite : oldFavorites) {
            favoriteItemService.deleteByGuestFavoriteId(oldFavorite.getId());
        }
        guestFavoriteRepository.deleteAll(oldFavorites);
    }
}