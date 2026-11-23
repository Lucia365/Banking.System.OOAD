
package com.banking.controller;

import com.banking.model.Account;
import java.util.List;

public class CustomerHomeController {

    private final AccountController accountController;

    public CustomerHomeController(
            AccountController accountController) {

        this.accountController = accountController;
    }

    // Deposit money
    public boolean depositMoney(String accountNumber, double amount) {
        return accountController.deposit(accountNumber, amount);
    }

    // Withdraw money
    public boolean withdrawMoney(String accountNumber, double amount) {
        return accountController.withdraw(accountNumber, amount);
    }

    // Check balance
    public Double viewBalance(String accountNumber) {
        return accountController.checkBalance(accountNumber);
    }

    // Search account
    public Account searchAccount(String accountNumber) {
        return accountController.findAccount(accountNumber);
    }

    // Retrieve accounts
    public List<Account> getAccounts() {
        return accountController.getAllAccounts();
    }
}
