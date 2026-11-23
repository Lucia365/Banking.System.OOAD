
package com.banking.view;

import com.banking.controller.BankingContext;
import com.banking.model.TransactionHistory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CustomerView {

    public void show(Stage stage) {

        Label title = new Label("CUSTOMER");

        title.setStyle("-fx-font-size: 25px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #17365D;");

        Label subtitle = new Label("Welcome to your account");

        Button depositButton = new Button("Deposit Funds");

        Button withdrawButton = new Button("Withdraw Funds");

        Button balanceButton = new Button("Check Balance");

        Button historyButton = new Button("Transaction History");

        Button logoutButton = new Button("Logout");

        Button[] buttons = {
                depositButton,
                withdrawButton,
                balanceButton,
                historyButton,
                logoutButton
        };

        for (Button button : buttons) {

            button.setPrefWidth(280);
            button.setPrefHeight(42);

            button.setStyle("-fx-background-color: #2874A6;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-background-radius: 8;");
        }

        // DEPOSIT FUNDS
        depositButton.setOnAction(e ->
                new TransactionView().show(stage, "Deposit")
        );

        // WITHDRAW FUNDS
        withdrawButton.setOnAction(e ->
                new TransactionView().show(stage, "Withdraw")
        );

        // CHECK BALANCE
        balanceButton.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Check Balance");
            dialog.setHeaderText("Account Balance Inquiry");
            dialog.setContentText("Enter Account Number:");

            dialog.showAndWait().ifPresent(input -> {

                String number = input.trim();

                if (number.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Account Number", "Please enter an account number.");
                    return;
                }

                Double balance = BankingContext.ACCOUNTS.checkBalance(number);

                if (balance == null) {

                    showAlert(Alert.AlertType.ERROR, "Account Not Found", "The account could not be found " + "or its balance could not be retrieved.");

                } else {

                    showAlert(Alert.AlertType.INFORMATION, "Account Balance",
                            String.format("Account Number: %s%nBalance: BWP %.2f", number, balance)
                    );
                }
            });
        });

        // TRANSACTION HISTORY FROM MYSQL
        historyButton.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Transaction History");
            dialog.setHeaderText("View Saved Transactions");
            dialog.setContentText("Enter Account Number:");

            dialog.showAndWait().ifPresent(input -> {

                String number = input.trim();

                if (number.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Account Number", "Please enter an account number.");
                    return;
                }

                try {

                    List<TransactionHistory> transactions = BankingContext.ACCOUNTS.getTransactionHistory(number);

                    StringBuilder details = new StringBuilder();

                    details.append("ACCOUNT: ")
                            .append(number)
                            .append("\n\n");

                    if (transactions.isEmpty()) {

                        details.append("No transactions found for this account.");

                    } else {

                        for (TransactionHistory transaction : transactions) {

                            details.append("Transaction ID: ")
                                    .append(transaction.getTransactionId())
                                    .append("\n");

                            details.append("Date: ")
                                    .append(transaction.getDate())
                                    .append("\n");

                            details.append("Type: ")
                                    .append(transaction.getType())
                                    .append("\n");

                            details.append(String.format(
                                    "Amount: BWP %.2f%n",
                                    transaction.getAmount()
                            ));

                            details.append(
                                    "--------------------------------\n"
                            );
                        }
                    }

                    showDetails(
                            "Transaction History",
                            details.toString()
                    );

                } catch (SQLException ex) {

                    showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to retrieve transactions:\n" + ex.getMessage());
                }
            });
        });

        // LOGOUT
        logoutButton.setOnAction(e -> {

            BankingContext.LOGIN.logout();

            new RoleSelectionView().show(stage);
        });

        VBox layout = new VBox(16);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(title, subtitle, depositButton, withdrawButton, balanceButton, historyButton, logoutButton);

        layout.setStyle("-fx-background-color: #F4F8FC;");

        Scene scene = new Scene(layout, 600, 550);

        stage.setTitle("Banking System - Customer");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String heading, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(heading);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDetails(String heading, String information) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle(heading);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);

        TextArea textArea = new TextArea(information);

        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(500, 380);

        dialog.getDialogPane().setContent(textArea);

        dialog.showAndWait();
    }
}
