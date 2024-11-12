package org.example.configuration;

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
        Product tShirt1 = new Product("Fnatic", "Comfortable T-Shirt", 19.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.T_SHIRTS, "fnaticTshirt.jpg");
        Product tShirt2 = new Product("NiP", "Casual T-Shirt", 24.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.T_SHIRTS, "nipTshirt.jpg");
        Product tShirt3 = new Product("Team Liquid", "Sporty T-Shirt", 29.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.T_SHIRTS, "liquidTshirt.jpg");

        Product hoodie1 = new Product("Astralis", "Warm Hoodie", 59.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.HOODIES, "astralisHoodie.jpg");
        Product hoodie2 = new Product("ESL", "Fashionable Hoodie", 44.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.HOODIES, "eslHoodie.jpg");
        Product hoodie3 = new Product("TSM", "Cozy Hoodie", 49.99, new HashMap<>() {{
            put(Size.S.getDisplayName(), 10); // S - 10 шт
            put(Size.M.getDisplayName(), 10); // M - 5 шт
            put(Size.L.getDisplayName(), 10); // L - 8 шт
        }}, Category.HOODIES, "tsmHoodie.jpg");

        productRepository.saveAll(List.of(tShirt1, tShirt2, tShirt3, hoodie1, hoodie2, hoodie3));
    }

    private void initializePromoCodes() {
//        PromoCode promo1 = new PromoCode("DISCOUNT10", 10);
//        PromoCode promo2 = new PromoCode("DISCOUNT20", 20);
//        PromoCode promo3 = new PromoCode("DISCOUNT15", 15);
//
//        promoCodeRepository.saveAll(List.of(promo1, promo2, promo3));
    }
}