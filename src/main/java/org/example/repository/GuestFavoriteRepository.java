package org.example.repository;

import org.example.model.GuestFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GuestFavoriteRepository extends JpaRepository<GuestFavorite, Long> {
    Optional<GuestFavorite> findByGuestIdentifier(String guestIdentifier);

    @Query("SELECT g FROM GuestFavorite g WHERE g.createdAt < :expirationTime")
    List<GuestFavorite> findOldGuestFavorites(@Param("expirationTime") LocalDateTime expirationTime);
}