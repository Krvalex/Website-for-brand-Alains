package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDateTime date;
    private String FIO;
    private String email;
    private String phone;
    private String city;
    private String deliveryMethod;
    private double totalSum;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(LocalDateTime date, String FIO, String email, String phone, String city, String deliveryMethod, double totalSum) {
        this.date = date;
        this.FIO = FIO;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.deliveryMethod = deliveryMethod;
        this.totalSum = totalSum;
    }

    public String getTotalSum() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ru", "RU"));
        return numberFormat.format(this.totalSum).replace("\u00A0", " ");
    }

    public String getDateFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return date.format(formatter);
    }
}
