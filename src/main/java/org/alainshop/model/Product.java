package org.alainshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alainshop.model.enums.Category;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private double price;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "size")
    @Column(name = "quantity")
    private Map<String, Integer> sizes; // Размер - количество

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imageFront;
    private String imageBack;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Product(String name, String description, double price, Map<String, Integer> sizes,
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

    public String getPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ru", "RU"));
        return numberFormat.format(this.price).replace("\u00A0", " ");
    }
}

