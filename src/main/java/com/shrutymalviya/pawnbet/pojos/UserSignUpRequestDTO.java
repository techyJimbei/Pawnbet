package com.shrutymalviya.pawnbet.pojos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequestDTO {
    private String email;
    private String username;
    private String password;
    private String profileImageUrl;
}
