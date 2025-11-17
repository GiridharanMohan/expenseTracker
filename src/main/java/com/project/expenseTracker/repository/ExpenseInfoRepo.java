package com.project.expenseTracker.repository;

import com.project.expenseTracker.model.ExpenseInfo;
import com.project.expenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseInfoRepo extends JpaRepository<ExpenseInfo, UUID> {
    List<ExpenseInfo> findAllExpenseByUser(User user);

    Optional<ExpenseInfo> findByUserAndId(User user, UUID id);
}
