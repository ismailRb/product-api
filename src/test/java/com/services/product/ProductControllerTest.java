package com.services.product;


import com.services.product.controller.ProductController;
import com.services.product.model.Product;
import com.services.product.service.ProductService;
import com.services.product.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

  class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
      void testCreateProduct_AdminAccess() throws Exception {
         Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantity(10);
        product.setPrice(100);

         when(jwtUtil.isAdmin("Bearer valid_jwt_token")).thenReturn(true);

         when(productService.createProduct(any(Product.class))).thenReturn(product);

          mockMvc.perform(post("/products")
                .contentType("application/json")
                .content("{ \"name\": \"Test Product\", \"price\": 100, \"quantity\": 10 }")
                .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"));  // Vérifie que le produit renvoyé a le nom attendu

    }

    @Test
      void testCreateProduct_NotAdmin() throws Exception {
         Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");


         when(jwtUtil.extractEmail("valid_jwt_token")).thenReturn("user@user.com");

         mockMvc.perform(post("/products")
                .contentType("application/json")
                .content("{ \"name\": \"Test Product\", \"price\": 100, \"quantity\": 10 }")
                .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isForbidden());  // Vérifier si l'accès est interdit pour l'utilisateur non admin
    }

    @Test
      void testGetProductById() throws Exception {

         Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

         when(productService.getProductById(1L)).thenReturn(java.util.Optional.of(product));

         mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
      void testDeleteProduct_NotAdmin() throws Exception {
         MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid_jwt_token");

         when(jwtUtil.extractEmail("valid_jwt_token")).thenReturn("user@user.com");

         mockMvc.perform(delete("/products/1")
                .requestAttr("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isForbidden());  // Vérifier que l'accès est interdit pour l'utilisateur non admin
    }

    @Test
      void testDeleteProduct_AdminAccess() throws Exception {
         MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid_jwt_token");

         when(jwtUtil.isAdmin("Bearer valid_jwt_token")).thenReturn(true);

         doNothing().when(productService).deleteProduct(1L);  // Simuler la suppression du produit avec l'ID 1


         mockMvc.perform(delete("/products/1")
                .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isNoContent());  // Vérifier que le produit a été supprimé
    }
}
