package org.example.repository;


import org.example.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> { //убрать List
    PromoCode findByCode(@Param("code") String code);

    boolean existsByCode(String code);
}
