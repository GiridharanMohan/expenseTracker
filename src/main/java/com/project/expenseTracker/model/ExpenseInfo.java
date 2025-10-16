package com.project.expenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Should not be empty")
    private String expenseName;

    @NotBlank(message = "Should not be empty")
    private String expenseType;

    @NotBlank
    @Min(value = 1, message = "Should not be zero")
    private BigDecimal price;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;
}
