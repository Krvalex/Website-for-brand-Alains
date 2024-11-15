package org.example.component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.model.enums.Size;
import org.example.repository.ProductRepository;
import org.example.repository.PromoCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    ProductRepository productRepository;
    PromoCodeRepository promoCodeRepository;

    @Override
    public void run(String... args) {
        initializeProducts();
        initializePromoCodes();
    }

    private void initializeProducts() {
//        productRepository.deleteAll();
        Product tShirt1 = new Product("KINGVON T-SHIRT", "KINGVON", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/kingvonFront.png");
        Product tShirt2 = new Product("SECOND T-SHIRT", "SECOND", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt1Front.jpg");
        Product tShirt3 = new Product("THIRD T-SHIRT", "THIRD", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt2Front.jpg");
        Product tShirt4 = new Product("FOURTH T-SHIRT", "FOURTH", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt3Front.jpg");
        Product tShirt5 = new Product("FIFTH T-SHIRT", "FIFTH", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt4Front.jpg");
        Product tShirt6 = new Product("SIXTH T-SHIRT", "SIXTH", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt5Front.jpg");
        Product tShirt7 = new Product("SEVENTH T-SHIRT", "SEVENTH", 1990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt6Front.jpg");

        Product hoodie1 = new Product("ZIPALAINS HOODIE", "ZIPALAINS", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/zipalainsFront.png");
        Product hoodie2 = new Product("VANDAL HOODIE", "VANDAL", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/vandalFront.png");
        Product hoodie3 = new Product("THIRD HOODIE", "THIRD", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/hoodie1Front.jpg");
        Product hoodie4 = new Product("FOURTH HOODIE", "FOURTH", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/hoodie2Front.jpg");
        Product hoodie5 = new Product("FIFTH HOODIE", "FIFTH", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/hoodie3Front.jpg");
        Product hoodie6 = new Product("SIXTH HOODIE", "SIXTH", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/hoodie4Front.jpg");
        Product hoodie7 = new Product("SEVENTH HOODIE", "SEVENTH", 3990, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10);
            put(Size.M.getDisplayName(), 10);
            put(Size.L.getDisplayName(), 10);
        }}, Category.HOODIES, "/images/hoodies/hoodie5Front.jpg");

        productRepository.saveAll(List.of(tShirt1, tShirt2, tShirt3, tShirt4, tShirt5, tShirt6, tShirt7,
                hoodie1, hoodie2, hoodie3, hoodie4, hoodie5, hoodie6, hoodie7));
    }

    private void initializePromoCodes() {
//        PromoCode promo1 = new PromoCode("DISCOUNT10", 10);
//        PromoCode promo2 = new PromoCode("DISCOUNT20", 20);
//        PromoCode promo3 = new PromoCode("DISCOUNT15", 15);
//
//        promoCodeRepository.saveAll(List.of(promo1, promo2, promo3));
    }
}