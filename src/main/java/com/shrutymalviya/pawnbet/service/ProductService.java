package com.shrutymalviya.pawnbet.service;


import com.shrutymalviya.pawnbet.model.Image;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.ProductStatus;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.ProductRequestDTO;
import com.shrutymalviya.pawnbet.pojos.ProductResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ProductResponseDTO listProduct(ProductRequestDTO productRequestDTO, String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = new Product();

        product.setTitle(productRequestDTO.getTitle());
        product.setDescription(productRequestDTO.getDescription());
        product.setBasePrice(productRequestDTO.getBasePrice());
        product.setStatus(ProductStatus.ACTIVE);
        product.setSeller(user);

        List<Image> images = (productRequestDTO.getImageUrls() != null)
                ? productRequestDTO.getImageUrls().stream()
                .map(url -> {
                    Image image = new Image();
                    image.setImageUrl(url);
                    image.setProduct(product);
                    return image;
                }).collect(Collectors.toList())
                : List.of();

        product.setImages(images);

        System.out.println("Saving product to DB...");
        Product saved = productRepository.save(product);
        System.out.println("Saved product ID: " + saved.getId());
        return new ProductResponseDTO(saved);

    }
}
