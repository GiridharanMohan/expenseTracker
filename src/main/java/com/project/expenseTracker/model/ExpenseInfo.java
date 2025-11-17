package com.project.expenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ExpenseInfo")
public class ExpenseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "expense name Should not be empty")
    private String expenseName;

    @NotBlank(message = "expense type Should not be empty")
    private String expenseType;


    @Digits(integer = 10, fraction = 2, message = "price Should not be zero")
    @NotNull(message = "Only numbers allowed")
    private BigDecimal price;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
