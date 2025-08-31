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

@CrossOrigin(origins = "*")
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
    public String signup(@RequestBody UserSignUpRequestDTO dto) {
        userService.signup(dto);
        return "User Signed in successfully";
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
                    new JwtLoginResponse(token)
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication){
        String username = authentication.getName();
        UserResponseDTO userResponseDTO = userService.getProfile(username);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/verify")
    public Boolean verifyToken(@RequestHeader("Authorization") String token){
        return jwtUtil.validateToken(token.replace("Bearer ", ""));
    }

}
