package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.CartItem;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    OrderRepository orderRepository;

    GuestCartService guestCartService;
    CartService cartService;

    public void createUserOrder(List<CartItem> cartItems, User user,
                                String FIO, String email, String phone, String city,
                                String deliveryMethod, double totalSum) {
        Order order = new Order(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), FIO, email, phone, city, deliveryMethod, totalSum);
        for (CartItem cartItem : cartItems) {
            order.getOrderItems().add(new OrderItem(cartItem.getProduct(), cartItem.getSize(), cartItem.getQuantity()));
        }
        order.setUser(user);
        orderRepository.save(order);
        cartService.clear(user);
        cartService.save(user.getCart());
    }

    public void createGuestOrder(List<CartItem> cartItems, String guestIdentifier,
                                 String FIO, String email, String phone, String city,
                                 String deliveryMethod, double totalSum) {
        Order order = new Order(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), FIO, email, phone, city, deliveryMethod, totalSum);
        for (CartItem cartItem : cartItems) {
            order.getOrderItems().add(new OrderItem(cartItem.getProduct(), cartItem.getSize(), cartItem.getQuantity()));
        }
        orderRepository.save(order);
        guestCartService.clear(guestIdentifier);
    }
}
