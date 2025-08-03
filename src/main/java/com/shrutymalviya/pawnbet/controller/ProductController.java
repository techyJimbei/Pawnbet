package com.shrutymalviya.pawnbet.controller;


import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.ProductRequestDTO;
import com.shrutymalviya.pawnbet.pojos.ProductResponseDTO;
import com.shrutymalviya.pawnbet.pojos.ProductUpdateDTO;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/product")
    public ResponseEntity<?> listProduct(@RequestBody ProductRequestDTO productRequestDTO, Authentication authentication) {
        try {
            String username = authentication.getName();
            ProductResponseDTO productResponseDto = productService.listProduct(productRequestDTO, username);
            return ResponseEntity.ok(productResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to list product: " + e.getMessage());
        }
    }

    @GetMapping("/product")
    public ResponseEntity<?> getProducts(Authentication authentication) {
        try{
            String username = authentication.getName();
            List<ProductResponseDTO> productResponseDTOS = productService.fetchProducts(username);
            return ResponseEntity.ok(productResponseDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch products: " + e.getMessage());
        }
    }

    @PutMapping("/product/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable int product_id, @RequestBody ProductUpdateDTO productUpdateDTO, Authentication authentication) {
        try{
            String username = authentication.getName();
            ProductResponseDTO productResponseDTO = productService.updateProduct(product_id, productUpdateDTO, username);
            return ResponseEntity.ok(productResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to update product: " + e.getMessage());
        }
    }

    @DeleteMapping("/product/{product_id}")
    public String deleteProduct(@PathVariable long product_id, Authentication authentication) {
        String username = authentication.getName();
        productService.deleteProduct(product_id, username);
        return "Product deleted successfully";
    }

}
