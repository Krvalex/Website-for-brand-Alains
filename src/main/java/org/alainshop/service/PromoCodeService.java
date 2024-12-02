package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.PromoCode;
import org.alainshop.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class PromoCodeService {

    PromoCodeRepository promoCodeRepository;

    public List<PromoCode> getAll() {
        return promoCodeRepository.findAll();
    }

    public void add(PromoCode promoCode) {
        if (!promoCodeRepository.existsByCode(promoCode.getCode())) {
            promoCodeRepository.save(promoCode);
        }
    }

    public void delete(Long id) {
        promoCodeRepository.deleteById(id);
    }

    public PromoCode findByCode(String promoCode) {
        return promoCodeRepository.findByCode(promoCode);
    }


    public double getDiscountedSum(PromoCode promoCode, double originalSum) {
        return originalSum - (originalSum * promoCode.getDiscount() / 100);
    }
}
