package org.example.repository;


import org.example.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    List<PromoCode> findByCode(@Param("code") String code);
}
