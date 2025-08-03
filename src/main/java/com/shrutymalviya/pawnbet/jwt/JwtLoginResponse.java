package com.shrutymalviya.pawnbet.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtLoginResponse {
    private String message;
    private String token;
}
