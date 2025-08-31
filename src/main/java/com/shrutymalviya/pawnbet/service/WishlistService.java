package com.shrutymalviya.pawnbet.service;

import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.model.Wishlist;
import com.shrutymalviya.pawnbet.pojos.WishlistRequestDTO;
import com.shrutymalviya.pawnbet.pojos.WishlistResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import com.shrutymalviya.pawnbet.repositrory.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addWishlistProduct(WishlistRequestDTO wishlistRequestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This user does not exist"));
        Product product = productRepository.findById(wishlistRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("This product does not exist"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
    }


    public void deleteWishlistProduct(String username, Long productId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("This user does not exist"));
        wishlistRepository.deleteByProductIdAndUserId(productId, user.getId());

    }

    public List<WishlistResponseDTO> getWishlistProducts(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("This user does not exist"));
        Long user_id = user.getId();

        List<Wishlist> wishlist = wishlistRepository.findByUserId(user_id);
        return wishlist.stream().map(WishlistResponseDTO::new).collect(Collectors.toList());
    }
}
