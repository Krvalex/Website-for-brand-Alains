package org.alainshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guest_favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestFavorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "guest_identifier", nullable = false, unique = true)
    private String guestIdentifier;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "guest_favorite_id")
    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public GuestFavorites(String guestIdentifier, LocalDateTime createdAt) {
        this.guestIdentifier = guestIdentifier;
        this.createdAt = createdAt;
    }
}
