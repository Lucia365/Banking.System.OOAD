
package com.banking.model;

public class InvestmentAccount extends Account {

    private double interestRate;
    private double minInitialDeposit;

    public InvestmentAccount(String accountNumber,
                             double balance,
                             String branch,
                             double interestRate,
                             double minInitialDeposit) {

        super(accountNumber, balance, branch);
        this.interestRate = interestRate;
        this.minInitialDeposit = minInitialDeposit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
        }
    }

    public double getMinInitialDeposit() {
        return minInitialDeposit;
    }

    public void setMinInitialDeposit(double minInitialDeposit) {
        if (minInitialDeposit >= 0) {
            this.minInitialDeposit = minInitialDeposit;
        }
    }

    public double calculateInterest() {
        return balance * interestRate;
    }

    @Override
    public String toString() {
        return "InvestmentAccount{" +
                "interestRate=" + interestRate +
                ", minInitialDeposit=" + minInitialDeposit +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}
