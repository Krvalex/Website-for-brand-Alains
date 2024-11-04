package org.example.configuration;

import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.model.enums.Size;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public DataInitializer(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void run(String... args) {
        initializeProducts();
    }

    private void initializeProducts() {
//        productRepository.deleteAll();
//        cartRepository.deleteAll();
        Product tShirt1 = new Product("Fnatic", "Comfortable T-Shirt", 19.99, 10, Size.M, Category.T_SHIRTS, "fnaticTshirt.jpg");
        Product tShirt4 = new Product("Fnatic", "Comfortable T-Shirt", 19.99, 10, Size.L, Category.T_SHIRTS, "fnaticTshirt.jpg");
        Product tShirt2 = new Product("NiP", "Casual T-Shirt", 24.99, 10, Size.L, Category.T_SHIRTS, "nipTshirt.jpg");
        Product tShirt3 = new Product("Team Liquid", "Sporty T-Shirt", 29.99, 10, Size.S, Category.T_SHIRTS, "liquidTshirt.jpg");

        Product hoodie1 = new Product("Astralis", "Warm Hoodie", 59.99, 10, Size.M, Category.HOODIES, "astralisHoodie.jpg");
        Product hoodie2 = new Product("ESL", "Fashionable Hoodie", 44.99, 10, Size.L, Category.HOODIES, "eslHoodie.jpg");
        Product hoodie3 = new Product("TSM", "Cozy Hoodie", 49.99, 10, Size.S, Category.HOODIES, "tsmHoodie.jpg");

        productRepository.saveAll(List.of(tShirt1, tShirt2, tShirt3, tShirt4, hoodie1, hoodie2, hoodie3));
    }
}