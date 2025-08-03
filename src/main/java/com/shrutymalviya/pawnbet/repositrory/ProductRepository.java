package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller);

}
