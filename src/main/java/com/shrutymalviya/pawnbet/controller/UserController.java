package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.jwt.JwtLoginResponse;
import com.shrutymalviya.pawnbet.jwt.JwtUtil;
import com.shrutymalviya.pawnbet.pojos.UserLoginRequestDTO;
import com.shrutymalviya.pawnbet.pojos.UserResponseDTO;
import com.shrutymalviya.pawnbet.pojos.UserSignUpRequestDTO;
import com.shrutymalviya.pawnbet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserSignUpRequestDTO dto) {
        UserResponseDTO response = userService.signup(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok().body(
                    new JwtLoginResponse("Login successful", token)
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
