
package com.banking.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {

    protected String customerId;
    protected String firstName;
    protected String surname;
    protected String address;
    protected List<Account> account;
    protected String customerType;

    // CONSTRUCTOR
    public Customer(
            String customerId,
            String firstName,
            String surname,
            String address,
            List<Account> account,
            String customerType) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;

        this.account = account == null
                ? new ArrayList<>()
                : new ArrayList<>(account);

        this.customerType = customerType;
    }

    // GET CUSTOMER ID
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // GET FIRST NAME
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // GET SURNAME
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // GET ADDRESS
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // GET CUSTOMER TYPE
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    // GET CUSTOMER ACCOUNTS
    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account == null
                ? new ArrayList<>()
                : new ArrayList<>(account);
    }

    // ADD ACCOUNT TO CUSTOMER
    public void addAccount(Account newAccount) {

        if (newAccount != null) {
            account.add(newAccount);
        }
    }

    // OPTIONAL CONVENIENCE METHOD
    public List<Account> getAccounts() {
        return account;
    }

    // DISPLAY CUSTOMER DETAILS
    @Override
    public String toString() {

        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", customerType='" + customerType + '\'' +
                ", accounts=" + account.size() +
                '}';
    }
}
