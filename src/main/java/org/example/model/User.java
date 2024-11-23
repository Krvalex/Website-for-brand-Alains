package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.enums.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_cart_id")
    private Cart cart = new Cart();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_favorite_id")
    private Favorites favorites = new Favorites();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public String getPhoneNumber() {
        String normalized = phoneNumber.replaceAll("[^\\d]", "");

        if (normalized.startsWith("8")) {
            normalized = "7" + normalized.substring(1);
        } else if (!normalized.startsWith("7")) {
            return "";
        }

        if (normalized.length() != 11) {
            return "";
        }

        String formatted = String.format("+7 (%s) %s %s %s",
                normalized.substring(1, 4),
                normalized.substring(4, 7),
                normalized.substring(7, 9),
                normalized.substring(9, 11));
        return formatted;
    }
}

