
package com.banking.controller;

public final class BankingContext {

    // Shared Login Controller
    public static final LoginController LOGIN = new LoginController();

    // Shared Customer Controller
    public static final CustomerController CUSTOMERS = new CustomerController();

    // Shared Account Controller
    public static final AccountController ACCOUNTS = new AccountController();

    // Administrator Controller
    public static final AdministratorController ADMIN = new AdministratorController(CUSTOMERS, ACCOUNTS);

    // Customer Home Controller
    public static final CustomerHomeController CUSTOMER_HOME = new CustomerHomeController(ACCOUNTS);

    // Prevent creating BankingContext objects
    private BankingContext() {
    }
}
