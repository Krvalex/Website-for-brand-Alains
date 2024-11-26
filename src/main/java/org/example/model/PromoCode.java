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
    private Long Id;

    private String code;
    private double discount;

    public String getFormatDiscount() {
        return String.format("%.0f %%", discount);
    }
}
