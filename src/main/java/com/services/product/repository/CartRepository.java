package com.services.product.repository;
import com.services.product.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Trouver le panier d'un utilisateur
    Cart findByUserId(Long userId);
}