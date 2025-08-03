package com.shrutymalviya.pawnbet.pojos;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {
    private long id;
    private String text;
    private UserResponseDTO user;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> replies;
}
