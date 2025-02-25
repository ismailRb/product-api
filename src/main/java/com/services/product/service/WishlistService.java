package com.services.product.service;

import com.services.product.model.Product;
import com.services.product.model.User;
import com.services.product.model.Wishlist;
import com.services.product.repository.ProductRepository;
import com.services.product.repository.UserRepository;
import com.services.product.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Wishlist getWishlist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user).orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            return wishlistRepository.save(newWishlist);
        });
    }

    public Wishlist addProductToWishlist(Long userId, Long productId) {
        Wishlist wishlist = getWishlist(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        wishlist.getProducts().add(product);
        return wishlistRepository.save(wishlist);
    }

    public Wishlist removeProductFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = getWishlist(userId);
        wishlist.getProducts().removeIf(p -> p.getId().equals(productId));
        return wishlistRepository.save(wishlist);
    }
}
