
package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.ChequeAccount;

import java.util.List;

public class AdministratorController {

    private final CustomerController customerController;
    private final AccountController accountController;

    public AdministratorController(
            CustomerController customerController,
            AccountController accountController) {

        this.customerController = customerController;
        this.accountController = accountController;
    }

    // REGISTER CUSTOMER

    public boolean registerCustomer(Customer customer) {
        return customerController.registerCustomer(customer);
    }

    // OPEN SAVINGS OR INVESTMENT ACCOUNT

    public boolean openAccount(Customer customer,
                               Account account) {

        return accountController.openAccount(
                customer,
                account
        );
    }

    // OPEN CHEQUE ACCOUNT WITH EMPLOYMENT DETAILS

    public boolean openChequeAccount(
            Customer customer,
            ChequeAccount account,
            String employerName,
            String employerAddress,
            double monthlySalary) {

        return accountController.openChequeAccount(
                customer,
                account,
                employerName,
                employerAddress,
                monthlySalary
        );
    }

    // VIEW ALL CUSTOMERS

    public List<Customer> viewCustomers() {
        return customerController.getAllCustomers();
    }

    // VIEW ALL ACCOUNTS

    public List<Account> viewAccounts() {
        return accountController.getAllAccounts();
    }

    // SEARCH CUSTOMER

    public Customer searchCustomer(String customerId) {
        return customerController.findCustomer(customerId);
    }

    // SEARCH ACCOUNT

    public Account searchAccount(String accountNumber) {
        return accountController.findAccount(accountNumber);
    }

    // TOTAL CUSTOMERS

    public int getTotalCustomers() {
        return customerController.getTotalCustomers();
    }

    // TOTAL ACCOUNTS

    public int getTotalAccounts() {
        return accountController.getTotalAccounts();
    }
}
