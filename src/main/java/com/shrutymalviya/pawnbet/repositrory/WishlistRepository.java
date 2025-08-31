package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> user(User user);

    List<Wishlist> findByUserId(Long userId);

    @Modifying
    @Query("delete from Wishlist w where w.product.id = :productId and w.user.id = :userId")
    void deleteByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);


    Boolean existsByUserOrProduct(User user, Product product);

    @Query("SELECT w.product.id FROM Wishlist w WHERE w.user = :user")
    Set<Long> findProductIdsByUser(@Param("user") User user);
}
