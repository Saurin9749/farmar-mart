package com.shoppingapp.product_service.Controller;

import com.shoppingapp.product_service.model.Expense;
import com.shoppingapp.product_service.repository.ExpenseRepository;
import com.shoppingapp.product_service.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseService expenseService, ExpenseRepository expenseRepository) {
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
    }
    @PostMapping
    public ResponseEntity addExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Object> updateExpense(@RequestBody  Expense expense) {
        expenseService.updateExpense(expense);
        return ResponseEntity.ok().build();

        Expense savedExpense = expenseRepository.findById(expense.getId()).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Expense by ID %s", expense.getId())));

        savedExpense.setExpenseName(expense.getExpenseName());
        savedExpense.setExpenseCategory(expense.getExpenseCategory());
        savedExpense.getExpenseAmount(expense.getExpenseAmount());

        expenseRepository.save(expense);
    }
    public List<Expense> getAllExpense() {

    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        ResponseEntity.ok(expenseService.getAllExpenses());
    }

    public void getExpenseByName() {}

    public void deleteExpense() {

    }

}
