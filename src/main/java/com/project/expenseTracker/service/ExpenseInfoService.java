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
        ExpenseInfo expenseInfo = expenseInfoRepo.findByUserAndId(user, id)
                .orElseThrow(() -> new ExpenseNotFoundException("Invalid ID, failed to update"));
        expenseInfo.setExpenseName(expense.getExpenseName());
        expenseInfo.setExpenseType(expense.getExpenseType());
        expenseInfo.setUpdatedOn(LocalDateTime.now());
        expenseInfo.setPrice(expense.getPrice());
        expenseInfoRepo.save(expenseInfo);
        log.info("End of updateExpense()");
        return ResponseEntity.status(HttpStatus.OK).body("Expense updated successfully");
    }

    public ResponseEntity<String> deleteExpense(UUID id) {
        User user = jwtUtil.getUserFromToken();
        log.info("Deleting expense for user : {}", user.getUsername());
        ExpenseInfo expenseInfo = expenseInfoRepo.findByUserAndId(user, id)
                .orElseThrow(() -> new ExpenseNotFoundException("Invalid ID, expense not found"));
        expenseInfoRepo.deleteById(expenseInfo.getId());
        log.info("End of deleteExpense()");
        return ResponseEntity.status(HttpStatus.OK).body("Expense deleted successfully");
    }


//    paginated
//    public ResponseEntity<Page<ExpenseInfo>> getAllExpenseInPages(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.status(HttpStatus.OK).body(expenseInfoRepo.findAll(pageable));
//    }
}
