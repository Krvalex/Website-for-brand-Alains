package org.example.controller;

import org.example.model.PromoCode;
import org.example.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promo")
public class PromoCodeController {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @PostMapping("/apply")
    public ResponseEntity<Double> applyPromoCode(@RequestParam String code, @RequestParam double originalSum) {
        PromoCode promoCode = promoCodeRepository.findByCode(code);
        System.out.println(originalSum);
        if (promoCode != null) {
            double discount = promoCode.getDiscount();
            double discountedSum = originalSum - (originalSum * discount / 100);
            return ResponseEntity.ok(discountedSum);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(originalSum);
        }
    }
}