package com.project.expenseTracker.controller;

import com.project.expenseTracker.model.ExpenseDto;
import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.service.ExpenseInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseInfoController {

    @Autowired
    ExpenseInfoService expenseInfoService;

    @GetMapping(value = "/")
    public ResponseEntity<List<ExpenseDto>> getAllExpense() {
        return expenseInfoService.getAllExpense();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ExpenseDto> getExpense(@PathVariable UUID id) {
        return expenseInfoService.getExpense(id);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseInfo expense) {
        log.info("Expense: {}", expense);
        return expenseInfoService.addExpense(expense);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateExpense(@Valid @RequestBody ExpenseInfo expense, @PathVariable UUID id) {
        log.info("Expense: {}, ID: {}", expense, id);
        return expenseInfoService.updateExpense(expense, id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id) {
        log.info("ID: {}", id);
        return expenseInfoService.deleteExpense(id);
    }


//    paginated
//    @GetMapping("/page")
//    public ResponseEntity<Page<ExpenseInfo>> getExpenseByPage(@RequestParam int page, @RequestParam int size) {
//        return expenseInfoService.getAllExpenseInPages(page, size);
//    }

}
