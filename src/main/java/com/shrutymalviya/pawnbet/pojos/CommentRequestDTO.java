package com.shrutymalviya.pawnbet.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private String text;
    private Long productId;
    private Long parentId;
}

