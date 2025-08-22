package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller);

    List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrTagContainingIgnoreCase(String title, String description, String tag);

    @Query("SELECT p FROM Product p LEFT JOIN p.bids b GROUP BY p ORDER BY COUNT(b) DESC")
    List<Product> findTop10TrendingProducts(Pageable pageable);
}
