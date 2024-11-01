package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.enums.Category;
import org.example.model.enums.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String productDescription;
    private double productPrice;

    @Enumerated(EnumType.STRING)
    private Size productSize;

    @Enumerated(EnumType.STRING)
    private Category productCategory;

    private String productImage;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Product(String productName, String productDescription, double productPrice, Size productSize,
                   Category productCategory, String productImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.productCategory = productCategory;
        this.productImage = productImage;
    }

}

