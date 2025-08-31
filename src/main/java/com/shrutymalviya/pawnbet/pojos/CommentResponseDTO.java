package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class CommentResponseDTO {
    private long id;
    private String text;
    private UserResponseDTO user;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> replies;

    public CommentResponseDTO(Comment saved){
        this.id = saved.getId();
        this.text = saved.getText();
        this.user = new UserResponseDTO(saved.getUser());
        this.createdAt = saved.getCreatedAt();
        if (saved.getChildren() != null && !saved.getChildren().isEmpty()) {
            this.replies = saved.getChildren().stream()
                    .map(CommentResponseDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.replies = new ArrayList<>();
        }
    }

}
