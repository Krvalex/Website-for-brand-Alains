package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDetails {

    private String deliveryAddress;
    private String paymentMethod;

}