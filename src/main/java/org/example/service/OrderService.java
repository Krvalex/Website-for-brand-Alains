package org.example.service;

import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<List<Product>> getAllProductsInOrders(List<Order> orders) {
        List<List<Product>> allProductsInOrders = new ArrayList<>();
        for (Order order : orders) {
            List<Long> productIds = order.getProducts_id();
            List<Product> products = productService.getAllProductsById(productIds);
            allProductsInOrders.add(products); // Добавляем список товаров в общий список
        }
        return allProductsInOrders;
    }

    public List<Double> getSumPriceOfListProducts(List<List<Product>> allProductsInOrders) {
        List<Double> totalPrices = new ArrayList<>();
        for (List<Product> products : allProductsInOrders) {
            double totalPrice = products.stream().mapToDouble(Product::getProductPrice).sum();
            totalPrices.add(totalPrice);
        }
        return totalPrices;
    }
}
