package com.project.expenseTracker.repository;

import com.project.expenseTracker.model.ExpenseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseInfoRepo extends JpaRepository<ExpenseInfo, UUID> {
}
