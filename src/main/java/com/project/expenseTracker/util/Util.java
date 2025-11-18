package com.project.expenseTracker.util;

import com.project.expenseTracker.model.ExpenseDto;
import com.project.expenseTracker.model.ExpenseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Util {

    public ExpenseDto toExpenseDto(ExpenseInfo expense) {
        return new ExpenseDto(expense.getId(), expense.getExpenseName(),
                expense.getExpenseType(), expense.getPrice(), expense.getCreatedOn(),
                expense.getUpdatedOn());
    }
}
