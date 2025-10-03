package com.project.expenseTracker.controller;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.service.ExpenseInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/expenses")
public class ExpenseInfoController {

    @Autowired
    ExpenseInfoService expenseInfoService;

    @GetMapping("/")
    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        return expenseInfoService.getAllExpense();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseInfo> getExpense(@PathVariable UUID id) {
        return expenseInfoService.getExpense(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseInfo expense) {
        log.info("Expense: {}", expense);
        return expenseInfoService.addExpense(expense);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateExpense(@Valid @RequestBody ExpenseInfo expense, @PathVariable UUID id) {
        log.info("Expense: {}, ID: {}", expense, id);
        return expenseInfoService.updateExpense(expense, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpense(@PathVariable UUID id) {
        log.info("ID: {}", id);
        return expenseInfoService.deleteExpense(id);
    }

}
