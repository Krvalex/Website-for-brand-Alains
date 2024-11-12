package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.repository.FavoriteItemRepository;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class FavoriteItemService {

    FavoriteItemRepository favoriteItemRepository;

    public void deleteByGuestFavoriteId(Long guestFavoriteId) {
        favoriteItemRepository.deleteByGuestFavoriteId(guestFavoriteId);
    }
}