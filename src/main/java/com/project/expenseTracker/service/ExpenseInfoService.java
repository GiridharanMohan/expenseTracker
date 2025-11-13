package com.project.expenseTracker.service;

import com.project.expenseTracker.exception.ExpenseNotFoundException;
import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.model.User;
import com.project.expenseTracker.repository.ExpenseInfoRepo;
import com.project.expenseTracker.repository.UserRepo;
import com.project.expenseTracker.util.JwtUtil;
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
    private ExpenseInfoRepo expenseInfoRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        User user = jwtUtil.getUserFromToken();
        log.info("Fetching all expense for user : {}", user.getUsername());
        List<ExpenseInfo> listOfExpenses = expenseInfoRepo.findAllExpenseByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(listOfExpenses);
    }

    public ResponseEntity<ExpenseInfo> getExpense(UUID id) {
        User user = jwtUtil.getUserFromToken();
        log.info("Fetching expense for user : {}", user.getUsername());
        Optional<ExpenseInfo> expense = expenseInfoRepo.findByUserAndId(user, id);
        return expense.map(expenseInfo -> ResponseEntity.status(HttpStatus.OK).body(expenseInfo)).orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
    }

    public ResponseEntity<String> addExpense(ExpenseInfo expenseInfo) {
        User user = jwtUtil.getUserFromToken();
        log.info("Adding expense for user : {}", user.getUsername());
        String msg = "Failed to add Expense, invalid expense.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (expenseInfo != null) {
            expenseInfo.setCreatedOn(LocalDateTime.now());
            expenseInfo.setUpdatedOn(LocalDateTime.now());
            expenseInfo.setUser(user);
            expenseInfoRepo.save(expenseInfo);
            msg = "Expense added successfully";
            httpStatus = HttpStatus.OK;
        }
        log.info("End of addExpense()");
        return ResponseEntity.status(httpStatus).body(msg);
    }

    public ResponseEntity<String> updateExpense(ExpenseInfo expense, UUID id) {
        User user = jwtUtil.getUserFromToken();
        log.info("Updating expense for user : {}", user.getUsername());
        Optional<ExpenseInfo> optionalExpenseInfo = expenseInfoRepo.findByUserAndId(user, id);
        if (optionalExpenseInfo.isEmpty()) {
            log.error("failed to update for user : {}", user.getUsername());
            throw new ExpenseNotFoundException("Invalid ID, failed to update");
        }
        ExpenseInfo exp = optionalExpenseInfo.get();
        exp.setExpenseName(expense.getExpenseName());
        exp.setExpenseType(expense.getExpenseType());
        exp.setUpdatedOn(LocalDateTime.now());
        exp.setPrice(expense.getPrice());
        expenseInfoRepo.save(exp);
        log.info("End of updateExpense()");
        return ResponseEntity.status(HttpStatus.OK).body("Expense updated successfully");
    }

    public ResponseEntity<String> deleteExpense(UUID id) {
        User user = jwtUtil.getUserFromToken();
        log.info("Deleting expense for user : {}", user.getUsername());
        Optional<ExpenseInfo> expenseInfo = expenseInfoRepo.findByUserAndId(user, id);
        if (expenseInfo.isPresent()) {
            expenseInfoRepo.deleteById(id);
            log.info("End of deleteExpense()");
            return ResponseEntity.status(HttpStatus.OK).body("Expense with ID: " + id + " deleted successfully");
        } else {
            log.error("ID: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expense with ID: " + id + " not found");
        }
    }


//    paginated
//    public ResponseEntity<Page<ExpenseInfo>> getAllExpenseInPages(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.status(HttpStatus.OK).body(expenseInfoRepo.findAll(pageable));
//    }
}
