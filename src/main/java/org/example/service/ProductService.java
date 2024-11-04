package org.example.service;

import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.model.enums.Size;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProductsById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    public double sum(List<Product> products) {
        double sum = 0;
        for (Product product : products) {
            sum += product.getProductPrice();
        }
        return sum;
    }

    public void decrementProduct(List<Product> products) {
        for (Product product : products) {
            product.setProductQuantity(product.getProductQuantity() - 1);
            productRepository.save(product);
        }
    }

    public List<Product> getUniqueProducts(List<Product> products) {
        List<Product> uniqueProducts = new ArrayList<>();
        Set<String> productNames = new HashSet<>();
        for (Product product : products) {
            String productName = product.getProductName();
            if (!productNames.contains(productName)) {
                productNames.add(productName);
                uniqueProducts.add(product);
            }
        }
        return uniqueProducts;
    }

    public List<Product> getAllProductsByName(String productName) {
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if (product.getProductName().equals(productName)) {
                products.add(product);
            }
        }
        return products;
    }

    public List<Size> getUniqueSizeByProducts(List<Product> products) {
        List<Size> uniqueSizes = new ArrayList<>();
        for (Product product : products) {
            if (!uniqueSizes.contains(product.getProductSize())) {
                uniqueSizes.add(product.getProductSize());
            }
        }
        return uniqueSizes;
    }

    public Product findByProductNameAndProductSize(String productName, Size size) {
        Product productR = null;
        for (Product product : productRepository.findAll()) {
            if (product.getProductName().equals(productName) && product.getProductSize().equals(size)) {
                productR = product;
                break;
            }
        }
        return productR;
    }
}
