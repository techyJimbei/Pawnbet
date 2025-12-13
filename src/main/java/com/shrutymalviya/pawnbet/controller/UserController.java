package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.jwt.JwtLoginResponse;
import com.shrutymalviya.pawnbet.jwt.JwtUtil;
import com.shrutymalviya.pawnbet.pojos.UserLoginRequestDTO;
import com.shrutymalviya.pawnbet.pojos.UserResponseDTO;
import com.shrutymalviya.pawnbet.pojos.UserSignUpRequestDTO;
import com.shrutymalviya.pawnbet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public ResponseEntity<?> signup(@RequestBody UserSignUpRequestDTO dto) {
        try {
            userService.signup(dto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO dto) {
        try {
            System.out.println("=== Login Attempt ===");
            System.out.println("Username: " + dto.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            System.out.println("Token generated successfully");
            System.out.println("Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

            return ResponseEntity.ok().body(new JwtLoginResponse(token));

        } catch (BadCredentialsException ex) {
            System.err.println("Login failed: Invalid credentials");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception ex) {
            System.err.println("Login error: " + ex.getMessage());
            ex.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            // Get authentication from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("=== Profile Request ===");
            System.out.println("Authentication: " + authentication);
            System.out.println("Is Authenticated: " + (authentication != null && authentication.isAuthenticated()));

            // Check if authentication exists and is valid
            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {

                System.err.println("Profile access denied: Not authenticated");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String username = authentication.getName();
            System.out.println("Username from auth: " + username);

            UserResponseDTO userResponseDTO = userService.getProfile(username);

            if (userResponseDTO == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            return ResponseEntity.ok().body(userResponseDTO);

        } catch (Exception e) {
            System.err.println("Profile error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("error", "Invalid authorization header");
                return ResponseEntity.ok(response);
            }

            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (isValid) {
                response.put("username", jwtUtil.extractUsername(token));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}