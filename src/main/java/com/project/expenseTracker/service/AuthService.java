package com.project.expenseTracker.service;

import com.project.expenseTracker.model.User;
import com.project.expenseTracker.repository.UserRepo;
import com.project.expenseTracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;


    public ResponseEntity<?> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepo.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User Registered successfully","token",token));
    }


    public ResponseEntity<?> isValidUser(Map<String, String> credentials) {
        String password = credentials.get("password");
        String email = credentials.get("email");

        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, please sign up");
        }

        //user.get().getPassword().equals(password)
        if(!passwordEncoder.matches(password, user.get().getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        String token = jwtUtil.generateToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token",token));
    }
}
