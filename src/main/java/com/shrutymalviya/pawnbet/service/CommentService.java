package com.shrutymalviya.pawnbet.service;

import com.shrutymalviya.pawnbet.model.Comment;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.CommentRequestDTO;
import com.shrutymalviya.pawnbet.pojos.CommentResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.CommentRepository;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        Product product = productRepository.findById(commentRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        Comment comment = new Comment();

        comment.setText(commentRequestDTO.getText());
        comment.setUser(user);
        comment.setProduct(product);

        if(commentRequestDTO.getParentId() != null){
            Comment parent = commentRepository.findById(commentRequestDTO.getParentId()).orElseThrow(() -> new RuntimeException("Parent not found"));
            comment.setParent(parent);
        }

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDTO(saved);

    }

    public List<CommentResponseDTO> getAllComments(long product_id) {
        Product product = productRepository.findById(product_id).orElseThrow(() -> new RuntimeException("Product not found"));

        List<Comment> comments = commentRepository.findByProductAndParentIsNull(product);
        return comments.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    public CommentResponseDTO updateComment(CommentRequestDTO commentRequestDTO, long comment_id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this comment");
        }

        if (commentRequestDTO.getText() != null) {
            comment.setText(commentRequestDTO.getText());
        }

        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDTO(updatedComment);
    }


    public void deleteComment(long comment_id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this comment");
        }

        commentRepository.deleteById(comment_id);
    }
}
