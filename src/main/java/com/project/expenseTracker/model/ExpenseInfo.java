package com.project.expenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "ExpenseInfo")
public class ExpenseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Should not be empty")
    private String expenseName;

    @NotBlank(message = "Should not be empty")
    private String expenseType;

    @Min(1)
    private float price;

    private Date date;
}
