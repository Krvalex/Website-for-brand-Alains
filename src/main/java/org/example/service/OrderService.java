package org.example.service;

import org.example.model.CartItem;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

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
