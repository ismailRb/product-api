package com.services.product.controller;

import com.services.product.mesagges.ApiResult;
import com.services.product.model.Cart;
import com.services.product.model.User;
import com.services.product.repository.UserRepository;
import com.services.product.security.JwtUtil;
import com.services.product.service.CartService;
import com.services.product.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private final CartService cartService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private final UserService userService;

    @Operation(summary = "Add product to cart", description = "Add a product to the user's shopping cart")
    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResult<Cart>> addProductToCart(

            @PathVariable Long productId,
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);
        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));
        Cart updatedCart = cartService.addProductToCart(userConnected.get().getId(), productId);
        return ResponseEntity.ok(new ApiResult<>(updatedCart));
    }

    @Operation(summary = "Remove product from cart", description = "Remove a product from the user's shopping cart")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResult<Cart>> removeProductFromCart(

            @PathVariable Long productId,

            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);
        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));


        Cart updatedCart = cartService.removeProductFromCart(userConnected.get().getId(), productId);
        return ResponseEntity.ok(new ApiResult<>(updatedCart));
    }

    @Operation(summary = "Get user's cart", description = "Retrieve the shopping cart for a specific user")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResult<Cart>> getCart(
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Optional<User> userConnected = userService.getUserByEmail(email);
        if (userConnected.isEmpty())
            return ResponseEntity.status(400).body(new ApiResult<>("User not found"));

        Cart cart = cartService.getCartByUserId(userConnected.get().getId());
        if (cart != null) {
            return ResponseEntity.ok(new ApiResult<>(cart));
        }
        return ResponseEntity.status(404).body(new ApiResult<>("Cart not found"));
    }
}
