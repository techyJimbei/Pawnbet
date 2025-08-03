package com.shrutymalviya.pawnbet.controller;


import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.ProductRequestDTO;
import com.shrutymalviya.pawnbet.pojos.ProductResponseDTO;
import com.shrutymalviya.pawnbet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<?> listProduct(@RequestBody ProductRequestDTO productRequestDTO, Authentication authentication) {
        System.out.println("==> listProduct() controller called");
        try {
            String username = authentication.getName();
            ProductResponseDTO productResponseDto = productService.listProduct(productRequestDTO, username);
            return ResponseEntity.ok(productResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to list product: " + e.getMessage());
        }
    }

}
