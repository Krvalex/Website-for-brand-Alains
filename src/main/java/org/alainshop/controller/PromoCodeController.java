package org.alainshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.PromoCode;
import org.alainshop.service.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequiredArgsConstructor
@RequestMapping("promo")
public class PromoCodeController {

    PromoCodeService promoCodeService;

    @PostMapping("/apply")
    public ResponseEntity<Double> applyPromoCode(@RequestParam String code, @RequestParam double originalSum) {
        PromoCode promoCode = promoCodeService.findByCode(code);
        if (promoCode != null) {
            return ResponseEntity.ok(promoCodeService.getDiscountedSum(promoCode, originalSum));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(originalSum);
        }
    }
}