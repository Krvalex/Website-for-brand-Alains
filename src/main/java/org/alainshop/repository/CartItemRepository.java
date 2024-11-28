package org.alainshop.repository;

import jakarta.transaction.Transactional;
import org.alainshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE guest_cart_id = :guestCartId", nativeQuery = true)
    void deleteByGuestCartId(@Param("guestCartId") Long guestCartId);
}