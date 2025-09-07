package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Order;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllBySeller(User seller);

    Boolean existsByProduct(Product product);

    Optional<Order> findByProduct(Product product);
}
