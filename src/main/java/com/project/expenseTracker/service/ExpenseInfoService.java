package com.project.expenseTracker.service;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.repository.ExpenseInfoRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseInfoService {

    @Autowired
    ExpenseInfoRepo expenseInfoRepo;

    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        List<ExpenseInfo> listOfExpenses = expenseInfoRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfExpenses);
    }

    public ResponseEntity<String> addExpense(ExpenseInfo expenseInfo) {
        String msg = "Expense added successfully";
        if(expenseInfo != null){
            msg = "Expense added successfully";
            expenseInfoRepo.save(expenseInfo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    public ResponseEntity<String> updateExpense(ExpenseInfo expense, int id) {
        Optional<ExpenseInfo> optionalExpenseInfo = expenseInfoRepo.findById(id);
        String msg = "failed to update, invalid ID!!";
        if(optionalExpenseInfo.isPresent()){
            ExpenseInfo exp = optionalExpenseInfo.get();
            exp.setExpenseName(expense.getExpenseName());
            exp.setExpenseType(expense.getExpenseType());
            exp.setDate(expense.getDate());
            exp.setPrice(expense.getPrice());
            expenseInfoRepo.save(exp);
            msg = "successfully updated";
        }
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    public ResponseEntity<ExpenseInfo> getExpense(int id) {
        Optional<ExpenseInfo> expense = expenseInfoRepo.findById(id);
        return expense.map(expenseInfo -> ResponseEntity.status(HttpStatus.OK).body(expenseInfo)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<Object> deleteExpense(int id) {
        Optional<ExpenseInfo> expenseInfo = expenseInfoRepo.findById(id);
        if(expenseInfo.isPresent()){
            ExpenseInfo expense = expenseInfo.get();
            expenseInfoRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully\n"+expense);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense with ID: "+id+" not found");
        }
    }
}
