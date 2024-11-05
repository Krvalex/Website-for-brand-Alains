package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"promocodes\"")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long promocodeId;

    private String code;
    private double discountPercentage;

    public PromoCode(String code, double discountPercentage) {
        this.code = code;
        this.discountPercentage = discountPercentage;
    }
}
