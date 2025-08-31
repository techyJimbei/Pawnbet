package com.shrutymalviya.pawnbet.controller;


import com.shrutymalviya.pawnbet.pojos.*;
import com.shrutymalviya.pawnbet.service.ProductService;
import org.apache.coyote.Response;
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
    public ResponseEntity<?> getMyProducts(Authentication authentication) {
        try{
            String username = authentication.getName();
            List<ProductResponseDTO> productResponseDTOS = productService.getMyProducts(username);
            return ResponseEntity.ok(productResponseDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch products: " + e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(Authentication authentication){
        try{
            String username = authentication.getName();
            List<ProductResponseDTO> productsResponseDTOs = productService.getAllProducts(username);
            return ResponseEntity.ok(productsResponseDTOs);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to fetch products: " + e.getMessage());
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
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

    @GetMapping("/product/trending")
    public ResponseEntity<List<ProductResponseDTO>> getTrendingProducts(){
        List<ProductResponseDTO> trendingProducts = productService.getTrendingProducts();
        return ResponseEntity.ok(trendingProducts);
    }

    @PostMapping("/product/{id}/auction")
    public ResponseEntity<?> addAuctionDetails(@PathVariable long id,
                                               @RequestBody AuctionScheduleRequestDTO auctionScheduleRequestDTO,
                                               Authentication authentication){
        String username = authentication.getName();
        productService.addAuctionDetails(id, username, auctionScheduleRequestDTO);
        return ResponseEntity.ok("Auction details added successfully");
    }

    @GetMapping("/product/{id}/auction")
    public ResponseEntity<?> getAuctionDetails(@PathVariable long id, Authentication authentication){
        String username = authentication.getName();
        AuctionScheduleResponseDTO auctionScheduleResponseDTO = productService.getAuctionDetails(username, id);
        return ResponseEntity.ok(auctionScheduleResponseDTO);
    }


}
