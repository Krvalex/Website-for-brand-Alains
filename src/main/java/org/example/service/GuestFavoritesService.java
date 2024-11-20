package org.example.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.FavoriteItem;
import org.example.model.GuestFavorites;
import org.example.model.Product;
import org.example.repository.GuestFavoritesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class GuestFavoritesService {

    GuestFavoritesRepository guestFavoritesRepository;

    FavoriteItemService favoriteItemService;
    ProductService productService;

    public GuestFavorites getOrCreate(String guestIdentifier, HttpSession session) {
        if (guestIdentifier == null) {
            guestIdentifier = UUID.randomUUID().toString();
            session.setAttribute("guestIdentifier", guestIdentifier);
        }
        final String identifier = guestIdentifier;
        return guestFavoritesRepository.findByGuestIdentifier(identifier)
                .orElseGet(() -> create(identifier));
    }

    public void saveIfNotAlreadyInFavorite(String guestIdentifier, Long productId, HttpSession session) {
        GuestFavorites guestFavorites = getOrCreate(guestIdentifier, session);
        if (guestFavorites.getFavoriteItems().stream().noneMatch(f -> f.getProduct().getId().equals(productId))) {
            Product product = productService.getById(productId);
            guestFavorites.getFavoriteItems().add(new FavoriteItem(product));
            guestFavoritesRepository.save(guestFavorites);
        }
    }

    public List<Product> getProducts(String guestIdentifier, HttpSession session) {
        GuestFavorites guestFavorites = getOrCreate(guestIdentifier, session);
        return guestFavorites.getFavoriteItems().stream()
                .map(FavoriteItem::getProduct)
                .toList();
    }

    public void deleteProduct(String guestIdentifier, Long favoriteItemId, HttpSession session) {
        GuestFavorites guestFavorites = getOrCreate(guestIdentifier, session);
        guestFavorites.getFavoriteItems().removeIf(favoriteItem -> favoriteItem.getId().equals(favoriteItemId));
        guestFavoritesRepository.save(guestFavorites);
    }

    private GuestFavorites create(String guestIdentifier) {
        GuestFavorites newFavorite = new GuestFavorites(guestIdentifier, LocalDateTime.now());
        return guestFavoritesRepository.save(newFavorite);
    }

    @Transactional
    public void cleanUpIfOld(LocalDateTime expirationTime) {
        List<GuestFavorites> oldFavorites = guestFavoritesRepository.findOldGuestFavorites(expirationTime);
        for (GuestFavorites oldFavorite : oldFavorites) {
            favoriteItemService.deleteByGuestFavoriteId(oldFavorite.getId());
        }
        guestFavoritesRepository.deleteAll(oldFavorites);
    }
}