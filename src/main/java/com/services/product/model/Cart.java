package com.services.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the cart", example = "1", required = true)
    private Long id;

    @ManyToOne
    @Schema(description = "The user associated with the cart", required = true)
    private User user;

    @ManyToMany
    @Schema(description = "List of products in the cart", required = true)
    private List<Product> products = new ArrayList<>();

    @Schema(description = "Timestamp when the cart was created", example = "2025-02-25T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the cart was last updated", example = "2025-02-25T12:00:00")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
