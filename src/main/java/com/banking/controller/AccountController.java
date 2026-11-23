
package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;
import com.banking.model.InvestmentAccount;
import com.banking.model.ChequeAccount;
import com.banking.model.TransactionHistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountController {

    private final List<Account> accounts;

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    private final Map<String, String> employerNames;
    private final Map<String, String> employerAddresses;
    private final Map<String, Double> monthlySalaries;

    public AccountController() {
        accounts = new ArrayList<>();
        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();
        employerNames = new HashMap<>();
        employerAddresses = new HashMap<>();
        monthlySalaries = new HashMap<>();
    }
    // OPEN NORMAL BANK ACCOUNT
    public boolean openAccount(Customer customer, Account account) {
        if (customer == null || account == null) {
            return false;
        }
        String number = account.getAccountNumber();
        if (number == null ||
                number.isBlank() ||
                !Double.isFinite(account.getBalance()) ||
                account.getBalance() < 0) {
            return false;
        }
        // CHEQUE ACCOUNTS NEED EMPLOYMENT DETAILS
        if (account instanceof ChequeAccount) {
            return false;
        }
        // INVESTMENT MINIMUM DEPOSIT
        if (account instanceof InvestmentAccount &&
                account.getBalance() < 500) {
            return false;
        }
        try {
            if (accountDAO.accountExists(number)) {
                return false;
            }
            if (!accountDAO.addAccount(account)) {
                return false;
            }
            customer.addAccount(account);
            accounts.add(account);

            return true;

        } catch (SQLException e) {
            System.err.println("Account creation failed: " + e.getMessage());
            return false;
        }
    }
    // OPEN CHEQUE ACCOUNT
    public boolean openChequeAccount(
            Customer customer,
            ChequeAccount account,
            String employerName,
            String employerAddress,
            double monthlySalary) {

        if (customer == null || account == null) {
            return false;
        }

        if (employerName == null ||
                employerName.isBlank() ||
                employerAddress == null ||
                employerAddress.isBlank() ||
                !Double.isFinite(monthlySalary) ||
                monthlySalary <= 0) {
            return false;
        }

        String number = account.getAccountNumber();
        if (number == null ||
                number.isBlank() ||
                !Double.isFinite(account.getBalance()) ||
                account.getBalance() < 0) {
            return false;
        }

        try {

            if (accountDAO.accountExists(number)) {
                return false;
            }

            if (!accountDAO.addAccount(account)) {
                return false;
            }

            customer.addAccount(account);
            accounts.add(account);
            employerNames.put(number, employerName.trim());
            employerAddresses.put(number, employerAddress.trim());
            monthlySalaries.put(number, monthlySalary);
            return true;

        } catch (SQLException e) {
            System.err.println("Cheque account creation failed: " + e.getMessage());
            return false;
        }
    }

    // GET EMPLOYMENT INFORMATION
    public String getEmployerName(String accountNumber) {
        return employerNames.get(accountNumber);
    }

    public String getEmployerAddress(String accountNumber) {
        return employerAddresses.get(accountNumber);
    }

    public Double getMonthlySalary(String accountNumber) {
        return monthlySalaries.get(accountNumber);
    }

    // FIND ACCOUNT IN MEMORY
    public Account findAccount(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        for (Account account : accounts) {
            if (accountNumber.equals(account.getAccountNumber())) {
                return account;
            }
        }
        return null;
    }

    // DEPOSIT MONEY
    public boolean deposit(String accountNumber, double amount) {

        if (accountNumber == null ||
                accountNumber.isBlank() ||
                !Double.isFinite(amount) ||
                amount <= 0) {
            return false;
        }
        try {

            boolean successful = transactionDAO.processTransaction(accountNumber, amount, "Deposit");
            if (successful) {
                refreshAccountBalance(accountNumber);
            }
            return successful;

        } catch (SQLException e) {
            System.err.println("Deposit failed: " + e.getMessage());
            return false;
        }
    }
    // WITHDRAW MONEY
    public boolean withdraw(String accountNumber, double amount) {

        if (accountNumber == null ||
                accountNumber.isBlank() ||
                !Double.isFinite(amount) ||
                amount <= 0) {
            return false;
        }

        try {
            boolean successful = transactionDAO.processTransaction(accountNumber, amount, "Withdraw");
            if (successful) {
                refreshAccountBalance(accountNumber);
            }
            return successful;

        } catch (SQLException e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
            return false;
        }
    }

    // SYNCHRONIZE IN-MEMORY BALANCE
    private void refreshAccountBalance(String accountNumber) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return;
        }
        try {
            Double balance = accountDAO.getBalance(accountNumber);
            if (balance != null) {
                account.setBalance(balance);
            }
        } catch (SQLException e) {

            System.err.println("Could not refresh account balance: " + e.getMessage());
        }
    }
    // CHECK BALANCE DIRECTLY FROM MYSQL
    public Double checkBalance(String accountNumber) {
        if (accountNumber == null ||
                accountNumber.isBlank()) {
            return null;
        }
        try {
            return accountDAO.getBalance(accountNumber);
        } catch (SQLException e) {
            System.err.println("Balance check failed: " + e.getMessage());
            return null;
        }
    }

    // GET TRANSACTION HISTORY
    public List<TransactionHistory> getTransactionHistory(
            String accountNumber) throws SQLException {
        return transactionDAO.getTransactions(accountNumber);
    }

    // GET ALL ACCOUNTS IN MEMORY
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // COUNT ACCOUNTS FROM MYSQL
    public int getTotalAccounts() {
        try {
            return accountDAO.countAccounts();
        } catch (SQLException e) {
            System.err.println("Could not count accounts: " + e.getMessage());
            return accounts.size();
        }
    }
}
