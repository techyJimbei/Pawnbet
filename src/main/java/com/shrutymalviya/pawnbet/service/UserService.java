package com.shrutymalviya.pawnbet.service;


import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.UserLoginRequestDTO;
import com.shrutymalviya.pawnbet.pojos.UserResponseDTO;
import com.shrutymalviya.pawnbet.pojos.UserSignUpRequestDTO;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponseDTO signup(UserSignUpRequestDTO dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(dto.getEmail());
        user.setProfileImageUrl(dto.getProfileImageUrl());

        userRepository.save(user);
        return new UserResponseDTO(user);
    }

    public UserResponseDTO login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return new UserResponseDTO(user);
    }


}
