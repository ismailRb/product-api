package com.services.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the product", example = "1", required = true)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Unique product code", example = "P123456", required = true)
    private String code;

    @Column(nullable = false)
    @Schema(description = "Name of the product", example = "Laptop", required = true)
    private String name;

    @Schema(description = "Detailed description of the product", example = "A high-performance laptop with 16GB RAM and 512GB SSD.")
    private String description;

    @Schema(description = "URL of the product image", example = "https://example.com/laptop.jpg")
    private String image;

    @Schema(description = "Category of the product", example = "Electronics")
    private String category;

    @Column(nullable = false)
    @Schema(description = "Price of the product", example = "999.99", required = true)
    private double price;

    @Column(nullable = false)
    @Schema(description = "Quantity of the product available in stock", example = "50", required = true)
    private int quantity;

    @Schema(description = "Internal reference for the product", example = "REF-12345")
    private String internalReference;

    @Schema(description = "ID for external system integration", example = "98765")
    private Long shellId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Inventory status of the product", example = "INSTOCK", required = true)
    private InventoryStatus inventoryStatus;

    @Schema(description = "Average rating of the product based on customer reviews", example = "4.5")
    private double rating;

    @Schema(description = "Timestamp when the product was created", example = "2025-02-25T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the product was last updated", example = "2025-02-25T12:00:00")
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

@Schema(description = "The inventory status of a product")
enum InventoryStatus {
    @Schema(description = "Product is available in stock")
    INSTOCK,

    @Schema(description = "Product is running low in stock")
    LOWSTOCK,

    @Schema(description = "Product is out of stock")
    OUTOFSTOCK
}
