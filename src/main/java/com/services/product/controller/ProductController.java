package com.services.product.controller;

import com.services.product.mesagges.ApiResult;
import com.services.product.model.Product;
import com.services.product.security.JwtUtil;
import com.services.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public ResponseEntity<ApiResult<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(new ApiResult<>(productService.getAllProducts()));
    }

    @Operation(summary = "Get product by ID", description = "Retrieve product details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Product>> getProductById(
            @Parameter(description = "ID of the product to be fetched") @PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(new ApiResult<>(product)))
                .orElse(ResponseEntity.status(404).body(new ApiResult<>("Product not found")));
    }

    @Operation(summary = "Create a new product", description = "Create a new product if the user is an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User is not an admin")
    })
    @PostMapping
    public ResponseEntity<ApiResult<Product>> createProduct(
            @RequestBody Product product,
            @RequestHeader HttpHeaders headers) {
        if (!jwtUtil.isAdmin(headers.getFirst(HttpHeaders.AUTHORIZATION))) {
            return ResponseEntity.status(403).body(new ApiResult<>("Forbidden: User is not an admin"));
        }
        return ResponseEntity.ok(new ApiResult<>(productService.createProduct(product)));
    }

    @Operation(summary = "Update product", description = "Update an existing product by ID if the user is an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User is not an admin")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<Product>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            @RequestHeader HttpHeaders headers) {
        if (!jwtUtil.isAdmin(headers.getFirst(HttpHeaders.AUTHORIZATION))) {
            return ResponseEntity.status(403).body(new ApiResult<>("Forbidden: User is not an admin"));
        }
        return productService.updateProduct(id, product)
                .map(updatedProduct -> ResponseEntity.ok(new ApiResult<>(updatedProduct)))
                .orElse(ResponseEntity.status(404).body(new ApiResult<>("Product not found")));
    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID if the user is an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User is not an admin")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteProduct(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers) {
        if (!jwtUtil.isAdmin(headers.getFirst(HttpHeaders.AUTHORIZATION))) {
            return ResponseEntity.status(403).body(new ApiResult<>("Forbidden: User is not an admin"));
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
