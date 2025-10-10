package com.shoppingapp.product_service.service;

import com.shoppingapp.product_service.model.Expense;
import com.shoppingapp.product_service.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void  addExpense(Expense expense) {
        expenseRepository.insert(expense);

    }

    public void updateExpense(){

    }
    public List<Expense> getAllExpenses(){
       return expenseRepository.findAll();

    }
    public void getExpenseByName(){

    }
    public void deleteExpense(){

    }
}
