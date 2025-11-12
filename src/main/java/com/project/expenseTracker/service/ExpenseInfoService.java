package com.project.expenseTracker.service;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.model.User;
import com.project.expenseTracker.repository.ExpenseInfoRepo;
import com.project.expenseTracker.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        Optional<User> user = getUserFromToken();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ExpenseInfo> listOfExpenses = expenseInfoRepo.findAllExpenseByUser(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(listOfExpenses);
    }

    public ResponseEntity<ExpenseInfo> getExpense(UUID id) {
        Optional<User> user = getUserFromToken();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<ExpenseInfo> expense = expenseInfoRepo.findByUserAndId(user.get(), id);
        return expense.map(expenseInfo -> ResponseEntity.status(HttpStatus.OK).body(expenseInfo)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    public ResponseEntity<String> addExpense(ExpenseInfo expenseInfo) {
        log.info("Beginning of addExpense()");
        String msg = "Failed to add Expense, invalid expense.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (expenseInfo != null) {
            expenseInfo.setCreatedOn(LocalDateTime.now());
            expenseInfo.setUpdatedOn(LocalDateTime.now());
            expenseInfoRepo.save(expenseInfo);
            msg = "Expense with ID: " + expenseInfo.getId() + " created successfully";
            httpStatus = HttpStatus.OK;
        }
        log.info("End of addExpense()");
        return ResponseEntity.status(httpStatus).body(msg);
    }

    public ResponseEntity<String> updateExpense(ExpenseInfo expense, UUID id) {
        log.info("Beginning of updateExpense()");
        Optional<ExpenseInfo> optionalExpenseInfo = expenseInfoRepo.findById(id);
        String msg = "Failed to update, invalid ID!!";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (optionalExpenseInfo.isPresent()) {
            ExpenseInfo exp = optionalExpenseInfo.get();
            exp.setExpenseName(expense.getExpenseName());
            exp.setExpenseType(expense.getExpenseType());
            exp.setUpdatedOn(LocalDateTime.now());
            exp.setPrice(expense.getPrice());
            expenseInfoRepo.save(exp);
            msg = "successfully updated";
            httpStatus = HttpStatus.OK;
        }
        log.info("End of updateExpense()");
        return ResponseEntity.status(httpStatus).body(msg);
    }

    public ResponseEntity<String> deleteExpense(UUID id) {
        log.info("Beginning of deleteExpense()");
        Optional<ExpenseInfo> expenseInfo = expenseInfoRepo.findById(id);
        if (expenseInfo.isPresent()) {
            expenseInfoRepo.deleteById(id);
            log.info("End of deleteExpense()");
            return ResponseEntity.status(HttpStatus.OK).body("Expense with ID: " + id + " deleted successfully");
        } else {
            log.error("Expense: {}, ID: {}", expenseInfo, id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expense with ID: " + id + " not found");
        }
    }

    private Optional<User> getUserFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        return userRepo.findByEmail(auth.getName());
    }


//    paginated
//    public ResponseEntity<Page<ExpenseInfo>> getAllExpenseInPages(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.status(HttpStatus.OK).body(expenseInfoRepo.findAll(pageable));
//    }
}
