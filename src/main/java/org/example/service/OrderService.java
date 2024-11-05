package org.example.service;

import org.example.model.*;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

//    public List<List<Product>> getAllProductsInOrders(List<Order> orders) {
//        List<List<Product>> allProductsInOrders = new ArrayList<>();
//        for (Order order : orders) {
//            List<Long> productIds = order.getProducts_id();
//            List<Product> products = productService.getAllProductsById(productIds);
//            allProductsInOrders.add(products); // Добавляем список товаров в общий список
//        }
//        return allProductsInOrders;
//    }

    public List<Double> getSumPriceOfListProducts(List<List<Product>> allProductsInOrders) {
        List<Double> totalPrices = new ArrayList<>();
        for (List<Product> products : allProductsInOrders) {
            double totalPrice = products.stream().mapToDouble(Product::getProductPrice).sum();
            totalPrices.add(totalPrice);
        }
        return totalPrices;
    }

    public void createNewOrder(List<CartItem> cartItems, User user) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().withNano(0).withSecond(0));
        for (CartItem cartItem : cartItems) {
            order.getProducts().add(new OrderItem(cartItem.getProduct(), cartItem.getSize(), cartItem.getQuantity()));
        }
        order.setUser(user);
        orderRepository.save(order);
    }
}
