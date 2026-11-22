
package com.banking.model;

public class ChequeAccount extends Account {

    private String chequeNumber;
    private double monthlyFee;

    public ChequeAccount(String accountNumber,
                         double balance,
                         String branch,
                         String chequeNumber,
                         double monthlyFee) {

        super(accountNumber, balance, branch);
        this.chequeNumber = chequeNumber;
        this.monthlyFee = monthlyFee;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        if (monthlyFee >= 0) {
            this.monthlyFee = monthlyFee;
        }
    }

    @Override
    public boolean withdrawal(double amount) {
        return super.withdrawal(amount);
    }

    public void applyMonthlyFee() {
        if (monthlyFee >= 0 && balance >= monthlyFee) {
            balance -= monthlyFee;
            System.out.println("Monthly fee applied: " + monthlyFee);
        } else {
            System.out.println("Insufficient balance to apply monthly fee.");
        }
    }

    @Override
    public String toString() {
        return "ChequeAccount{" +
                "chequeNumber='" + chequeNumber + '\'' +
                ", monthlyFee=" + monthlyFee +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}
