package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    public void create(Product product, List<String> productSizes, List<Integer> productQuantities) {
        Map<String, Integer> sizes = new HashMap<>();
        for (int i = 0; i < productSizes.size(); i++) {
            sizes.put(productSizes.get(i), productQuantities.get(i));
        }
        product.setSizes(sizes);
        productRepository.save(product);
    }

}
