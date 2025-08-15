package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String profileImageUrl;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}