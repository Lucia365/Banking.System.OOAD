
package com.banking.controller;

import com.banking.dao.CustomerDAO;
import com.banking.model.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private final List<Customer> customers;
    private final CustomerDAO customerDAO;

    public CustomerController() {

        customers = new ArrayList<>();
        customerDAO = new CustomerDAO();

        // Load saved customers from MySQL
        loadCustomers();
    }

    // LOAD CUSTOMERS FROM DATABASE
    private void loadCustomers() {

        try {
            customers.clear();
            customers.addAll(customerDAO.getAllCustomers()
            );

            System.out.println("Customers loaded from MySQL: " + customers.size());

        } catch (SQLException e) {

            System.err.println("Failed to load customers: " + e.getMessage());
        }
    }

    // REGISTER NEW CUSTOMER
    public boolean registerCustomer(Customer customer) {

        if (customer == null) {
            return false;
        }

        String customerId = customer.getCustomerId();
        if (customerId == null ||
                customerId.isBlank()) {
            return false;
        }

        // Check customers already loaded in memory
        if (findCustomer(customerId) != null) {
            return false;
        }

        try {

            // Save customer permanently in MySQL
            boolean saved = customerDAO.addCustomer(customer);
            if (saved) {

                // Add to memory only after database success
                customers.add(customer);
                System.out.println("Customer registered successfully: " + customerId);
                return true;
            }

        } catch (SQLException e) {

            System.err.println("Customer registration failed: " + e.getMessage());
        }
        return false;
    }

    // FIND CUSTOMER BY ID
    public Customer findCustomer(String customerId) {

        if (customerId == null ||
                customerId.isBlank()) {
            return null;
        }

        // Search existing in-memory customers first
        for (Customer customer : customers) {

            if (customerId.equals(customer.getCustomerId())) {
                return customer;
            }
        }

        // Search MySQL if not found in memory
        try {

            Customer customer = customerDAO.findCustomer(customerId);

            if (customer != null) {
                customers.add(customer);
            }

            return customer;

        } catch (SQLException e) {

            System.err.println("Customer search failed: " + e.getMessage());
            return null;
        }
    }

    // VIEW ALL CUSTOMERS
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    // COUNT TOTAL CUSTOMERS
    public int getTotalCustomers() {
        return customers.size();
    }

    // REFRESH CUSTOMERS FROM MYSQL
    public void refreshCustomers() {
        loadCustomers();
    }
}
