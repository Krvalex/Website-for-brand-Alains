package org.alainshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "size")
    private String size;

    @Column(name = "quantity")
    private int quantity;

    public CartItem(Product product, String size, int quantity) {
        this.quantity = quantity;
        this.product = product;
        this.size = size;
    }

    public String getFormattedTotalPrice() {
        int pricePerUnit = Integer.parseInt(product.getPrice().replace(" ", ""));
        int totalPrice = pricePerUnit * quantity;
        return String.format("%,d", totalPrice).replace(",", " ");
    }
}
