
package com.banking.dao;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.IndividualCustomer;
import com.banking.model.CompanyCustomer;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE - ADD A NEW CUSTOMER
    public boolean addCustomer(Customer customer)
            throws SQLException {

        if (customer == null) {
            return false;
        }

        String sql = """
            INSERT INTO customers (
                customerId, firstName, surname,
                address, customerType,
                nationalId, dateOfBirth,
                companyName, companyAddress,
                registrationNumber
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setCustomerParameters(stmt, customer, false);

            return stmt.executeUpdate() > 0;
        }
    }

    // READ - FIND CUSTOMER BY ID
    public Customer findCustomer(String customerId)
            throws SQLException {

        String sql = """
            SELECT * FROM customers
            WHERE customerId = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        }

        return null;
    }

    // READ - GET ALL CUSTOMERS
    public List<Customer> getAllCustomers()
            throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = """
            SELECT * FROM customers
            ORDER BY customerId
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
        }

        return customers;
    }

    // READ - CHECK IF CUSTOMER EXISTS
    public boolean customerExists(String customerId)
            throws SQLException {

        String sql = """
            SELECT 1 FROM customers
            WHERE customerId = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // UPDATE - MODIFY CUSTOMER DETAILS
    public boolean updateCustomer(Customer customer)
            throws SQLException {

        if (customer == null) {
            return false;
        }

        String sql = """
            UPDATE customers
            SET firstName = ?,
                surname = ?,
                address = ?,
                customerType = ?,
                nationalId = ?,
                dateOfBirth = ?,
                companyName = ?,
                companyAddress = ?,
                registrationNumber = ?
            WHERE customerId = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setCustomerParameters(stmt, customer, true);

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE - REMOVE CUSTOMER
    public boolean deleteCustomer(String customerId)
            throws SQLException {

        String sql = """
            DELETE FROM customers
            WHERE customerId = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            return stmt.executeUpdate() > 0;
        }
    }

    // COUNT REGISTERED CUSTOMERS
    public int countCustomers() throws SQLException {

        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // SET PARAMETERS FOR INSERT OR UPDATE
    private void setCustomerParameters(
            PreparedStatement stmt,
            Customer customer,
            boolean update) throws SQLException {

        int index = 1;

        if (!update) {
            stmt.setString(index++, customer.getCustomerId());
        }

        stmt.setString(index++, customer.getFirstName());
        stmt.setString(index++, customer.getSurname());
        stmt.setString(index++, customer.getAddress());
        stmt.setString(index++, customer.getCustomerType());

        if (customer instanceof IndividualCustomer) {

            IndividualCustomer individual =
                    (IndividualCustomer) customer;

            stmt.setInt(index++, individual.getNationalId());
            stmt.setString(index++, individual.getDateOfBirth());

            stmt.setNull(index++, Types.VARCHAR);
            stmt.setNull(index++, Types.VARCHAR);
            stmt.setNull(index++, Types.INTEGER);

        } else if (customer instanceof CompanyCustomer) {

            CompanyCustomer company =
                    (CompanyCustomer) customer;

            stmt.setNull(index++, Types.INTEGER);
            stmt.setNull(index++, Types.VARCHAR);

            stmt.setString(index++, company.getCompanyName());
            stmt.setString(index++, company.getCompanyAddress());
            stmt.setInt(index++, company.getRegistrationNumber());

        } else {

            throw new SQLException(
                    "Unsupported customer type: " +
                            customer.getClass().getName()
            );
        }

        if (update) {
            stmt.setString(index, customer.getCustomerId());
        }
    }

    // CONVERT DATABASE RECORD TO CUSTOMER OBJECT
    private Customer mapCustomer(ResultSet rs)
            throws SQLException {

        String customerId = rs.getString("customerId");
        String firstName = rs.getString("firstName");
        String surname = rs.getString("surname");
        String address = rs.getString("address");
        String customerType = rs.getString("customerType");

        List<Account> accounts = new ArrayList<>();

        // CHECK COMPANY DETAILS
        String companyName = rs.getString("companyName");

        if (companyName != null) {

            return new CompanyCustomer(
                    customerId,
                    firstName,
                    surname,
                    address,
                    accounts,
                    customerType,
                    companyName,
                    rs.getString("companyAddress"),
                    rs.getInt("registrationNumber")
            );
        }

        // OTHERWISE RETURN INDIVIDUAL CUSTOMER
        return new IndividualCustomer(
                customerId,
                firstName,
                surname,
                address,
                accounts,
                customerType,
                rs.getInt("nationalId"),
                rs.getString("dateOfBirth")
        );
    }
}
