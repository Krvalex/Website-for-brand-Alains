package org.alainshop.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.FavoriteItem;
import org.alainshop.model.GuestFavorites;
import org.alainshop.model.Product;
import org.alainshop.model.enums.Category;
import org.alainshop.repository.GuestFavoritesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public GuestFavorites get(String guestIdentifier) {
        return guestFavoritesRepository.findByGuestIdentifier(guestIdentifier)
                .orElse(null);
    }

    public void saveIfNotAlreadyInFavorite(String guestIdentifier, Long productId, HttpSession session) {
        GuestFavorites guestFavorites = getOrCreate(guestIdentifier, session);
        if (guestFavorites.getFavoriteItems().stream().noneMatch(f -> f.getProduct().getId().equals(productId))) {
            Product product = productService.getById(productId);
            guestFavorites.getFavoriteItems().add(new FavoriteItem(product));
            guestFavoritesRepository.save(guestFavorites);
        }
    }

    public List<Product> getProducts(String guestIdentifier) {
        GuestFavorites guestFavorites = get(guestIdentifier);
        if (guestFavorites != null) {
            return guestFavorites.getFavoriteItems().stream()
                    .map(FavoriteItem::getProduct)
                    .toList();
        }
        return new ArrayList<>();
    }

    public void deleteProductById(String guestIdentifier, Long favoriteItemId) {
        GuestFavorites guestFavorites = get(guestIdentifier);
        List<FavoriteItem> favoriteItems = guestFavorites.getFavoriteItems();
        favoriteItems.removeIf(favoriteItem -> favoriteItem.getProduct().getId().equals(favoriteItemId));
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

    public List<Product> getProductsByCategory(String guestIdentifier, Category category) {
        GuestFavorites guestFavorites = guestFavoritesRepository.findByGuestIdentifier(guestIdentifier).orElse(null);
        if (guestFavorites == null) {
            return new ArrayList<>();
        }
        return guestFavorites.getFavoriteItems()
                .stream()
                .map(FavoriteItem::getProduct)
                .filter(product -> product.getCategory() == category) // Фильтрация по категории
                .collect(Collectors.toList());
    }

    public boolean isFavorite(String guestIdentifier, Product product) {
        List<FavoriteItem> favoriteItems = new ArrayList<>();
        GuestFavorites guestFavorites = get(guestIdentifier);
        if (guestFavorites != null) {
            favoriteItems = guestFavorites.getFavoriteItems();
        }
        for (FavoriteItem favoriteItem : favoriteItems) {
            if (favoriteItem.getProduct().getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }
}