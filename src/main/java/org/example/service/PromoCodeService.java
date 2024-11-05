package org.example.service;

import jakarta.persistence.NonUniqueResultException;
import org.example.model.PromoCode;
import org.example.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    public PromoCode validatePromoCode(String code) {
        List<PromoCode> promoCodes = promoCodeRepository.findByCode(code);
        if (promoCodes.size() == 1) {
            return promoCodes.get(0);
        } else if (promoCodes.size() > 1) {
            throw new NonUniqueResultException("Найдено несколько промокодов с одним и тем же кодом.");
        }
        return null;
    }

    public List<PromoCode> getAllPromocodes() {
        return promoCodeRepository.findAll();
    }

    public void add(PromoCode promoCode) {
        promoCodeRepository.save(promoCode);
    }

    public void delete(Long id) {
        promoCodeRepository.deleteById(id);
    }
}
