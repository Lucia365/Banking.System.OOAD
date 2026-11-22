
package com.banking.model;

import com.banking.interfaces.AccountOperations;

public abstract class Account implements AccountOperations {

    protected String accountNumber;
    protected double balance;
    protected String branch;

    // Constructor
    public Account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
    }

    // No-argument constructor
    public Account() {
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getBranch() {
        return branch;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(double balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            System.out.println("Balance cannot be negative.");
        }
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    // Deposit
    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Overloaded deposit
    public void deposit(double amount, String reference) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
            System.out.println("Reference: " + reference);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdrawal
    @Override
    public boolean withdrawal(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful.");
            return true;
        }

        System.out.println("Insufficient funds or invalid amount.");
        return false;
    }

    // Display balance
    public void displayBalance() {
        System.out.println("Balance: " + balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}
