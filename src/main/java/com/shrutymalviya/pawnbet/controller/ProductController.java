package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.pojos.*;
import com.shrutymalviya.pawnbet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<?> listProduct(@RequestBody ProductRequestDTO productRequestDTO, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            ProductResponseDTO productResponseDto = productService.listProduct(productRequestDTO, username);
            return ResponseEntity.ok(productResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(createError("Failed to list product: " + e.getMessage()));
        }
    }

    @GetMapping("/product/my")
    public ResponseEntity<?> getMyProducts(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            List<ProductResponseDTO> productResponseDTOS = productService.getMyProducts(username);
            return ResponseEntity.ok(productResponseDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to fetch products: " + e.getMessage()));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(Authentication authentication) {
        try {
            System.out.println("=== Get All Products Request ===");
            System.out.println("Authentication: " + authentication);
            System.out.println("Is Authenticated: " + (authentication != null && authentication.isAuthenticated()));

            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            System.out.println("Username: " + username);

            List<ProductResponseDTO> productsResponseDTOs = productService.getAllProducts(username);
            System.out.println("Products fetched: " + productsResponseDTOs.size());

            return ResponseEntity.ok(productsResponseDTOs);
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(createError("Failed to fetch products: " + e.getMessage()));
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        try {
            List<ProductResponseDTO> products = productService.searchProducts(keyword);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to search products: " + e.getMessage()));
        }
    }

    @PutMapping("/product/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable int product_id,
                                           @RequestBody ProductUpdateDTO productUpdateDTO,
                                           Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            ProductResponseDTO productResponseDTO = productService.updateProduct(product_id, productUpdateDTO, username);
            return ResponseEntity.ok(productResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/product/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long product_id, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            productService.deleteProduct(product_id, username);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to delete product: " + e.getMessage()));
        }
    }

    @GetMapping("/product/trending")
    public ResponseEntity<?> getTrendingProducts() {
        try {
            List<ProductResponseDTO> trendingProducts = productService.getTrendingProducts();
            return ResponseEntity.ok(trendingProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to fetch trending products: " + e.getMessage()));
        }
    }

    @PostMapping("/product/{id}/auction")
    public ResponseEntity<?> addAuctionDetails(@PathVariable long id,
                                               @RequestBody AuctionScheduleRequestDTO auctionScheduleRequestDTO,
                                               Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            AuctionScheduleResponseDTO auctionScheduleResponseDTO =
                    productService.addAuctionDetails(id, username, auctionScheduleRequestDTO);
            return ResponseEntity.ok(auctionScheduleResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to add auction details: " + e.getMessage()));
        }
    }

    @GetMapping("/product/{id}/auction")
    public ResponseEntity<?> getAuctionDetails(@PathVariable long id, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            AuctionScheduleResponseDTO auctionScheduleResponseDTO =
                    productService.getAuctionDetails(username, id);
            return ResponseEntity.ok(auctionScheduleResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to fetch auction details: " + e.getMessage()));
        }
    }

    @GetMapping("/product/auction/my")
    public ResponseEntity<?> getWinningAuctions(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createError("Authentication required"));
            }

            String username = authentication.getName();
            List<ProductResponseDTO> wonProducts = productService.getWinningAuctions(username);
            return ResponseEntity.ok(wonProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createError("Failed to fetch winning auctions: " + e.getMessage()));
        }
    }

    private Map<String, String> createError(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}