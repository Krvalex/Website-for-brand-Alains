package org.alainshop.repository;

import org.alainshop.model.GuestFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GuestFavoritesRepository extends JpaRepository<GuestFavorites, Long> {
    Optional<GuestFavorites> findByGuestIdentifier(String guestIdentifier);

    @Query("SELECT g FROM GuestFavorites g WHERE g.createdAt < :expirationTime")
    List<GuestFavorites> findOldGuestFavorites(@Param("expirationTime") LocalDateTime expirationTime);
}