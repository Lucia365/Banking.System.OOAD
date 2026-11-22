
package com.banking.model;

public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(String accountNumber,
                          double balance,
                          String branch,
                          double interestRate) {

        super(accountNumber, balance, branch);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
        }
    }

    public double calculateInterest() {
        return balance * interestRate;
    }

    // Savings account does not allow withdrawals
    @Override
    public boolean withdrawal(double amount) {
        System.out.println("Withdrawals are not allowed from Savings Account.");
        return false;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "interestRate=" + interestRate +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}
