package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.enums.Category;

import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String description;
    private String price;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "size")
    @Column(name = "quantity")
    private Map<String, Integer> sizes; // Размер - количество

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imageFront;
    private String imageBack;

    public Product(String name, String description, String price, Map<String, Integer> sizes,
                   Category category, String imageFront, String imageBack) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sizes = sizes;
        this.category = category;
        this.imageFront = imageFront;
        this.imageBack = imageBack;
    }

    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}

