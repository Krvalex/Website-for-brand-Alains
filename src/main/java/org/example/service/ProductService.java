package org.example.service;

import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProducts(Category category) {
        if (category == null) {
            return productRepository.findAll();
        } else {
            return productRepository.findByProductCategory(category);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void saveNewProduct(Product product, List<String> productSizes, List<Integer> productQuantities) {
        Map<String, Integer> sizes = new HashMap<>();
        for (int i = 0; i < productSizes.size(); i++) {
            sizes.put(productSizes.get(i), productQuantities.get(i));
        }
        product.setProductSizes(sizes);
        productRepository.save(product);
    }

}
