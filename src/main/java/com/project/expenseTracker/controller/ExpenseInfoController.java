package com.project.expenseTracker.controller;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.service.ExpenseInfoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "fetched data successfully"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema()))})
    public ResponseEntity<List<ExpenseInfo>> getAllExpense() {
        return expenseInfoService.getAllExpense();
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully retrieved data", content = @Content(schema = @Schema(implementation = ExpenseInfo.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema()))})
    public ResponseEntity<ExpenseInfo> getExpense(@PathVariable UUID id) {
        return expenseInfoService.getExpense(id);
    }

    //paginated
    @GetMapping("/page")
    public ResponseEntity<Page<ExpenseInfo>> getExpenseByPage(@RequestParam int page, @RequestParam int size) {
        return expenseInfoService.getAllExpenseInPages(page, size);
    }

    @PostMapping("/add")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, Invalid value!!", content = @Content(schema = @Schema()))})
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseInfo expense) {
        log.info("Expense: {}", expense);
        return expenseInfoService.addExpense(expense);
    }

    @PutMapping("/update/{id}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, Invalid value!!", content = @Content(schema = @Schema()))})
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
