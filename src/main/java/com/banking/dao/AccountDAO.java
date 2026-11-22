
package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // CREATE - SAVE NEW ACCOUNT
    public boolean addAccount(Account account)
            throws SQLException {

        if (account == null) {
            return false;
        }

        String sql = """
                INSERT INTO accounts
                (accountNumber, balance, branch, accountType)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setString(3, account.getBranch());
            stmt.setString(
                    4, account.getClass().getSimpleName()
            );

            return stmt.executeUpdate() > 0;
        }
    }

    // READ - GET ACCOUNT BALANCE
    public Double getBalance(String accountNumber)
            throws SQLException {

        String sql = """
                SELECT balance
                FROM accounts
                WHERE accountNumber = ?
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }

        return null;
    }

    // READ - CHECK WHETHER ACCOUNT EXISTS
    public boolean accountExists(String accountNumber)
            throws SQLException {

        String sql = """
                SELECT 1
                FROM accounts
                WHERE accountNumber = ?
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // UPDATE - ACCOUNT BALANCE
    public boolean updateBalance(
            String accountNumber,
            double newBalance) throws SQLException {

        if (!Double.isFinite(newBalance)
                || newBalance < 0) {
            return false;
        }

        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE accountNumber = ?
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE - REMOVE ACCOUNT
    public boolean deleteAccount(String accountNumber)
            throws SQLException {

        String sql = """
                DELETE FROM accounts
                WHERE accountNumber = ?
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            return stmt.executeUpdate() > 0;
        }
    }

    // READ - GET ALL ACCOUNT NUMBERS
    public List<String> getAllAccountNumbers()
            throws SQLException {

        List<String> accountNumbers = new ArrayList<>();

        String sql = """
                SELECT accountNumber
                FROM accounts
                ORDER BY accountNumber
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                accountNumbers.add(
                        rs.getString("accountNumber")
                );
            }
        }

        return accountNumbers;
    }

    // READ - COUNT ALL ACCOUNTS
    public int countAccounts() throws SQLException {

        String sql = "SELECT COUNT(*) FROM accounts";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        }
    }
}
