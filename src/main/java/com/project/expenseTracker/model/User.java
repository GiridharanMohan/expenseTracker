package com.project.expenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "should not be empty")
    @Column(name = "user_name")
    private String username;

    @NotBlank
    @Size(min = 8, message = "length should be at least 8 characters")
    @Column(name = "password")
    private String password;

    @Email(message = "Invalid email id format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
