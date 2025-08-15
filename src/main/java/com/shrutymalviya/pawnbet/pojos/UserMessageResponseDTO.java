package com.shrutymalviya.pawnbet.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMessageResponseDTO {
    private String message;
    private boolean success;

    public UserMessageResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}
