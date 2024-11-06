package org.example.model;

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
public class FavoriteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteItemId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public FavoriteItem(Product product) {
        this.product = product;
    }
}