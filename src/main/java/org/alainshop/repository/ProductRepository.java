package org.alainshop.repository;

import org.alainshop.model.Product;
import org.alainshop.model.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category productCategory);
    Product findByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);
}

