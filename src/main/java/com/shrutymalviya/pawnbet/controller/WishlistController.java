package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.pojos.WishlistRequestDTO;
import com.shrutymalviya.pawnbet.pojos.WishlistResponseDTO;
import com.shrutymalviya.pawnbet.service.WishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/wishlist")
    public ResponseEntity<?> addWishlistProduct(@RequestBody WishlistRequestDTO wishlist, Authentication authentication) {
        try {
            String username = authentication.getName();
            wishlistService.addWishlistProduct(wishlist, username);
            return ResponseEntity.ok("Wishlist Product added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wishlist could not be added: " + e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<?> deleteWishlistProduct(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            wishlistService.deleteWishlistProduct(username, id);
            return ResponseEntity.ok("Wishlist product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wishlist could not be deleted: " + e.getMessage());
        }
    }


    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishlistProducts(Authentication authentication) {
        try{
            String username = authentication.getName();
            List<WishlistResponseDTO> products = wishlistService.getWishlistProducts(username);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
