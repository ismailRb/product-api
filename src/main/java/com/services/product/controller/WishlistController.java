package com.services.product.controller;

import com.services.product.mesagges.ApiResult;
import com.services.product.model.User;
import com.services.product.model.Wishlist;
import com.services.product.repository.UserRepository;
import com.services.product.security.JwtUtil;
import com.services.product.service.UserService;
import com.services.product.service.WishlistService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wishList")
@RequiredArgsConstructor
public class WishlistController {

    @Autowired
    private final WishlistService wishlistService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final UserService userService;

    @Operation(summary = "Get wishlist", description = "Retrieve the wishlist for a specific user")
    @GetMapping()
    public ResponseEntity<ApiResult<Wishlist>> getWishlist(
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);
        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));

        Wishlist wishlist = wishlistService.getWishlist(userConnected.get().getId());
        if (wishlist != null) {
            return ResponseEntity.ok(new ApiResult<>(wishlist));
        }
        return ResponseEntity.status(404).body(new ApiResult<>("Wishlist not found"));
    }

    @Operation(summary = "Add product to wishlist", description = "Add a product to the user's wishlist")
    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResult<Wishlist>> addProductToWishlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);

        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(userConnected.get().getId(), productId);
        return ResponseEntity.ok(new ApiResult<>(updatedWishlist));
    }

    @Operation(summary = "Remove product from wishlist", description = "Remove a product from the user's wishlist")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResult<Wishlist>> removeProductFromWishlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {

        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);

        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));

        Wishlist updatedWishlist = wishlistService.removeProductFromWishlist(userConnected.get().getId(), productId);
        return ResponseEntity.ok(new ApiResult<>(updatedWishlist));
    }
}
