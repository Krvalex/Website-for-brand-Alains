package org.alainshop.component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.NewsPost;
import org.alainshop.model.Product;
import org.alainshop.model.PromoCode;
import org.alainshop.model.enums.Category;
import org.alainshop.model.enums.Size;
import org.alainshop.repository.NewsRepository;
import org.alainshop.repository.ProductRepository;
import org.alainshop.repository.PromoCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    ProductRepository productRepository;
    PromoCodeRepository promoCodeRepository;
    NewsRepository newsRepository;

    @Override
    public void run(String... args) {
        initializeProducts();
        initializePromoCodes();
        initializeNewsPosts();
    }

    private void initializeProducts() {
        productRepository.deleteAll();
        Product tShirt1 = new Product("KINGVON T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/kingvonFront.png", "/images/tshirts/kingvonBack.png");
        Product tShirt2 = new Product("SECOND T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt1Front.jpg", "/images/tshirts/tshirt1Back.jpg");
        Product tShirt3 = new Product("THIRD T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt2Front.jpg", "/images/tshirts/tshirt2Back.jpg");
        Product tShirt4 = new Product("FOURTH T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt3Front.jpg", "/images/tshirts/tshirt3Back.jpg");
        Product tShirt5 = new Product("FIFTH T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ТЕМНО-СЕРЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt4Front.jpg", "/images/tshirts/tshirt4Back.jpg");
        Product tShirt6 = new Product("SIXTH T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt5Front.jpg", "/images/tshirts/tshirt5Back.jpg");
        Product tShirt7 = new Product("SEVENTH T-SHIRT", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 1990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.T_SHIRTS, "/images/tshirts/tshirt6Front.jpg", "/images/tshirts/tshirt6Back.jpg");

        Product hoodie1 = new Product("ZIPALAINS HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/zipalainsFront.png", "/images/hoodies/zipalainsBack.png");
        Product hoodie2 = new Product("VANDAL HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ТЕМНО-СЕРЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/vandalFront.png", "/images/hoodies/vandalBack.png");
        Product hoodie3 = new Product("THIRD HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/hoodie1Front.jpg", "/images/hoodies/hoodie1Back.jpg");
        Product hoodie4 = new Product("FOURTH HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/hoodie2Front.jpg", "/images/hoodies/hoodie2Back.jpg");
        Product hoodie5 = new Product("FIFTH HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/hoodie3Front.jpg", "/images/hoodies/hoodie3Back.jpg");
        Product hoodie6 = new Product("SIXTH HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: БЕЛЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/hoodie4Front.jpg", "/images/hoodies/hoodie4Back.jpg");
        Product hoodie7 = new Product("SEVENTH HOODIE", """
                ТКАНЬ: КУЛИРНАЯ ГЛАДЬ
                ЦВЕТ: ЧЕРНЫЙ
                СОСТАВ: 100% ХЛОПОК
                ПЛОТНОСТЬ 240 ГР/М""", 3990, new HashMap<>() {{
            put(Size.XS.getDisplayName(), 0);
            put(Size.S.getDisplayName(), 5);
            put(Size.M.getDisplayName(), 7);
            put(Size.L.getDisplayName(), 10);
            put(Size.XL.getDisplayName(), 5);
            put(Size.XXL.getDisplayName(), 0);
        }}, Category.HOODIES, "/images/hoodies/hoodie5Front.jpg", "/images/hoodies/hoodie5Back.jpg");

        productRepository.saveAll(List.of(tShirt1, tShirt2, tShirt3, tShirt4, tShirt5, tShirt6, tShirt7,
                hoodie1, hoodie2, hoodie3, hoodie4, hoodie5, hoodie6, hoodie7));
    }

    private void initializePromoCodes() {
        PromoCode promo1 = new PromoCode("DISCOUNT10", 10);
        PromoCode promo2 = new PromoCode("DISCOUNT20", 20);
        PromoCode promo3 = new PromoCode("DISCOUNT15", 15);

        promoCodeRepository.saveAll(List.of(promo1, promo2, promo3));
    }

    private void initializeNewsPosts() {
        if (newsRepository.count() == 0) {
            List<NewsPost> defaultNews = List.of(
                    new NewsPost("Новая футболка!",
                            "Мы выпустили новую футболку с логотипом бренда\uD83D\uDE80",
                            "kingvon.png",
                            LocalDateTime.of(2025, 1, 15, 10, 0)),


                    new NewsPost("Скидка 20%\uD83D\uDD25",
                            "Дарим промокод WELCOME20 всем новым пользователям",
                            null,
                            LocalDateTime.of(2025, 3, 19, 10, 0)),


                    new NewsPost("Обновление сайта❗",
                            "Запустили новый раздел с уходом за одеждой. Выбирай тип одежды и смотри",
                            "uhod.png",
                            LocalDateTime.of(2025, 2, 23, 10, 0))
            );

            newsRepository.saveAll(defaultNews);
        }
    }
}