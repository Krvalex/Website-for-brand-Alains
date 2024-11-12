package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorite_item WHERE guest_favorite_id = :guestFavoriteId", nativeQuery = true)
    void deleteByGuestFavoriteId(@Param("guestFavoriteId") Long guestFavoriteId);
}