
package com.banking.dao;

import com.banking.model.TransactionHistory;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDAO {

    // SAVE TRANSACTION HISTORY
    public boolean addTransaction(
            String accountNumber,
            TransactionHistory transaction)
            throws SQLException {

        String sql = """
                INSERT INTO transactions
                (transactionId, accountNumber,
                 transactionDate, transactionType, amount)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, accountNumber);
            stmt.setString(3, transaction.getDate());
            stmt.setString(4, transaction.getType());
            stmt.setDouble(5, transaction.getAmount());

            return stmt.executeUpdate() == 1;
        }
    }

    // PROCESS DEPOSIT OR WITHDRAWAL
    // BALANCE AND HISTORY ARE SAVED TOGETHER
    public boolean processTransaction(
            String accountNumber,
            double amount,
            String type) throws SQLException {

        if (accountNumber == null ||
                accountNumber.isBlank() ||
                !Double.isFinite(amount) ||
                amount <= 0) {
            return false;
        }

        boolean withdrawal = "Withdraw".equalsIgnoreCase(type);

        if (!withdrawal && !"Deposit".equalsIgnoreCase(type)) {
            return false;
        }

        String selectSql = """
                SELECT balance, accountType
                FROM accounts
                WHERE accountNumber = ?
                FOR UPDATE
                """;

        String updateSql = """
                UPDATE accounts
                SET balance = ?
                WHERE accountNumber = ?
                """;

        String insertSql = """
                INSERT INTO transactions
                (transactionId, accountNumber,
                 transactionDate, transactionType, amount)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                double currentBalance;
                String accountType;

                // LOCK ACCOUNT ROW
                try (PreparedStatement stmt =
                             conn.prepareStatement(selectSql)) {

                    stmt.setString(1, accountNumber);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        currentBalance = rs.getDouble("balance");
                        accountType = rs.getString("accountType");
                    }
                }

                // SAVINGS ACCOUNTS CANNOT WITHDRAW
                if (withdrawal &&
                        "SavingsAccount".equals(accountType)) {
                    conn.rollback();
                    return false;
                }

                // CHECK AVAILABLE FUNDS
                if (withdrawal && currentBalance < amount) {
                    conn.rollback();
                    return false;
                }

                double newBalance = withdrawal
                        ? currentBalance - amount
                        : currentBalance + amount;

                if (!Double.isFinite(newBalance)) {
                    conn.rollback();
                    return false;
                }

                // UPDATE ACCOUNT BALANCE
                try (PreparedStatement stmt =
                             conn.prepareStatement(updateSql)) {

                    stmt.setDouble(1, newBalance);
                    stmt.setString(2, accountNumber);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                String transactionId =
                        UUID.randomUUID().toString();

                String transactionDate =
                        LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss"
                                )
                        );

                // SAVE TRANSACTION RECORD
                try (PreparedStatement stmt =
                             conn.prepareStatement(insertSql)) {

                    stmt.setString(1, transactionId);
                    stmt.setString(2, accountNumber);
                    stmt.setString(3, transactionDate);
                    stmt.setString(
                            4, withdrawal ? "Withdraw" : "Deposit"
                    );
                    stmt.setDouble(5, amount);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException ex) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    ex.addSuppressed(rollbackError);
                }

                throw ex;
            }
        }
    }

    // FIND TRANSACTION BY ID
    public TransactionHistory findTransaction(
            String transactionId) throws SQLException {

        String sql = """
                SELECT * FROM transactions
                WHERE transactionId = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transactionId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapTransaction(rs);
                }
            }
        }

        return null;
    }

    // GET ALL TRANSACTIONS FOR ONE ACCOUNT
    public List<TransactionHistory> getTransactions(
            String accountNumber) throws SQLException {

        List<TransactionHistory> transactions =
                new ArrayList<>();

        String sql = """
                SELECT * FROM transactions
                WHERE accountNumber = ?
                ORDER BY transactionDate DESC, transactionId DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    transactions.add(mapTransaction(rs));
                }
            }
        }

        return transactions;
    }

    // UPDATE TRANSACTION RECORD
    public boolean updateTransaction(
            TransactionHistory transaction)
            throws SQLException {

        String sql = """
                UPDATE transactions
                SET transactionDate = ?,
                    transactionType = ?,
                    amount = ?
                WHERE transactionId = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getType());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setString(4, transaction.getTransactionId());

            return stmt.executeUpdate() == 1;
        }
    }

    // DELETE TRANSACTION RECORD
    public boolean deleteTransaction(String transactionId)
            throws SQLException {

        String sql = """
                DELETE FROM transactions
                WHERE transactionId = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transactionId);

            return stmt.executeUpdate() == 1;
        }
    }

    // COUNT TRANSACTIONS
    public int countTransactions(String accountNumber)
            throws SQLException {

        String sql = """
                SELECT COUNT(*) FROM transactions
                WHERE accountNumber = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // CONVERT DATABASE ROW INTO MODEL OBJECT
    private TransactionHistory mapTransaction(ResultSet rs)
            throws SQLException {

        return new TransactionHistory(
                rs.getString("transactionId"),
                rs.getString("transactionDate"),
                rs.getString("transactionType"),
                rs.getDouble("amount")
        );
    }
}
