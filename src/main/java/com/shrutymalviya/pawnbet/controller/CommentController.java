package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.pojos.CommentRequestDTO;
import com.shrutymalviya.pawnbet.pojos.CommentResponseDTO;
import com.shrutymalviya.pawnbet.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequestDTO commentRequestDTO, Authentication authentication) {
       try{
           String username = authentication.getName();
           CommentResponseDTO commentResponseDTO = commentService.addComment(commentRequestDTO, username);
           return ResponseEntity.ok(commentResponseDTO);
       }
       catch(Exception e){
           return ResponseEntity.badRequest().body("Cannot add comment "+e.getMessage());
       }
    }

    @GetMapping("/comment/{product_id}")
    public ResponseEntity<?> getAllComments(@PathVariable long product_id) {
        try{
            List<CommentResponseDTO> comments = commentService.getAllComments(product_id);
            return ResponseEntity.ok(comments);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Cannot get comments "+e.getMessage());
        }
    }

    @PutMapping("/comment/{comment_id}")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable long comment_id,  Authentication authentication) {
        try{
            String username = authentication.getName();
            CommentResponseDTO commentResponseDTO = commentService.updateComment(commentRequestDTO, comment_id, username);
            return ResponseEntity.ok(commentResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Comment cannot be updated"+e.getMessage());
        }
    }

    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable long comment_id, Authentication authentication) {
        try{
            String username = authentication.getName();
            commentService.deleteComment(comment_id, username);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot delete comment "+e.getMessage());
        }
    }

}
