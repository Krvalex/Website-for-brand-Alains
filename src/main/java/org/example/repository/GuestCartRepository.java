package org.example.repository;

import org.example.model.GuestCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GuestCartRepository extends JpaRepository<GuestCart, Long> {
    Optional<GuestCart> findByGuestIdentifier(String guestIdentifier);

    @Query("SELECT g FROM GuestCart g WHERE g.createdAt < :expirationTime")
    List<GuestCart> findOldGuestCarts(@Param("expirationTime") LocalDateTime expirationTime);
}