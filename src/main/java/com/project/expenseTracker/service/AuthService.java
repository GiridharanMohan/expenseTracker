package com.project.expenseTracker.service;

import com.project.expenseTracker.model.User;
import com.project.expenseTracker.repository.UserRepo;
import com.project.expenseTracker.util.EmailSender;
import com.project.expenseTracker.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailSender emailSender;


    public ResponseEntity<?> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("checking existing user : {}", user.getEmail());
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            log.info("user already exists : {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        //email generator
        emailSender.accountCreationEmailSender(user.getEmail(), user.getUsername());
        log.info("new user added, mail sent to user : {}", user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User Registered successfully", "token", token));
    }


    public ResponseEntity<?> isValidUser(Map<String, String> credentials) {
        String password = credentials.get("password");
        String email = credentials.get("email");

        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, please sign up");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        String token = jwtUtil.generateToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token, "message", "Welcome "+user.get().getUsername()+"! You have logged in successfully."));
    }
}
