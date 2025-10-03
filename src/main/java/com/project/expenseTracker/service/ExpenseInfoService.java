package com.project.expenseTracker.service;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.repository.ExpenseInfoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ExpenseInfoService {

    @Autowired
    ExpenseInfoRepo expenseInfoRepo;

    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        List<ExpenseInfo> listOfExpenses = expenseInfoRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfExpenses);
    }

    public ResponseEntity<String> addExpense(ExpenseInfo expenseInfo) {
        log.info("Beginning of addExpense()");
        String msg = "Failed to add Expense, invalid expense.";
        if (expenseInfo != null) {
            expenseInfo.setCreatedOn(LocalDateTime.now());
            expenseInfo.setUpdatedOn(LocalDateTime.now());
            msg = "Expense added successfully";
            expenseInfoRepo.save(expenseInfo);
        }
        log.info("End of addExpense()");
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    public ResponseEntity<String> updateExpense(ExpenseInfo expense, int id) {
        log.info("Beginning of updateExpense()");
        Optional<ExpenseInfo> optionalExpenseInfo = expenseInfoRepo.findById(id);
        String msg = "Failed to update, invalid ID!!";
        if (optionalExpenseInfo.isPresent()) {
            ExpenseInfo exp = optionalExpenseInfo.get();
            exp.setExpenseName(expense.getExpenseName());
            exp.setExpenseType(expense.getExpenseType());
            exp.setUpdatedOn(LocalDateTime.now());
            exp.setPrice(expense.getPrice());
            expenseInfoRepo.save(exp);
            msg = "successfully updated";
        }
        log.info("End of updateExpense()");
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    public ResponseEntity<ExpenseInfo> getExpense(int id) {
        Optional<ExpenseInfo> expense = expenseInfoRepo.findById(id);
        return expense.map(expenseInfo -> ResponseEntity.status(HttpStatus.OK).body(expenseInfo)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<Object> deleteExpense(int id) {
        log.info("Beginning of deleteExpense()");
        Optional<ExpenseInfo> expenseInfo = expenseInfoRepo.findById(id);
        if (expenseInfo.isPresent()) {
            ExpenseInfo expense = expenseInfo.get();
            expenseInfoRepo.deleteById(id);
            log.info("End of deleteExpense()");
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully\n" + expense);
        } else {
            log.error("Expense: {}, ID: {}", expenseInfo, id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense with ID: " + id + " not found");
        }
    }
}
