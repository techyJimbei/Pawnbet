package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.User;

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

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
