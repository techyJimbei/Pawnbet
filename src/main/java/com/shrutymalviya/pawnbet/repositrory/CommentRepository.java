package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Comment;
import com.shrutymalviya.pawnbet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByProduct(Product product);
}
