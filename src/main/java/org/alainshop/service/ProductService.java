package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.Product;
import org.alainshop.model.enums.Category;
import org.alainshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByCategory(Category category) {
        if (category == null) {
            return productRepository.findAll();
        }
        return productRepository.findByCategory(category);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void create(String name, String description, double price,
                       List<String> sizes, List<Integer> quantities,
                       String category, String imageFront, String imageBack) {
        Map<String, Integer> sizeAndQuantities = new HashMap<>();
        for (int i = 0; i < sizes.size(); i++) {
            sizeAndQuantities.put(sizes.get(i), quantities.get(i));
        }
        Product product = new Product(name, description, price, sizeAndQuantities, Category.valueOf(category), imageFront, imageBack);
        productRepository.save(product);
    }

    public String getOldPrice(String price) {
        double oldPrice = Double.parseDouble(price.replace(" ", "")) + 1000;
        NumberFormat formatter = NumberFormat.getInstance(new Locale("ru", "RU"));
        return formatter.format(oldPrice);
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> searchByName(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}
