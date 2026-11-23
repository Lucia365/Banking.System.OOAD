
package com.banking.view;

import com.banking.controller.BankingContext;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransactionView {

    public void show(Stage stage, String transactionType) {
        Label title = new Label(transactionType.toUpperCase() + " FUNDS");
        title.setStyle("-fx-font-size: 24px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #17365D;");

        TextField accountNumber = new TextField();
        accountNumber.setPromptText("Account Number");

        TextField amount = new TextField();
        amount.setPromptText("Amount (BWP)");

        Button submitButton = new Button("Submit " + transactionType);
        Button backButton = new Button("Back");
        submitButton.setStyle("-fx-background-color: #17365D;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-background-radius: 8;");

        backButton.setStyle("-fx-background-color: #2874A6;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-background-radius: 8;");

        // SUBMIT TRANSACTION
        submitButton.setOnAction(e -> {

            String number = accountNumber.getText().trim();
            String amountText = amount.getText().trim();
            if (number.isEmpty() || amountText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Enter the account number and amount.");
                return;
            }
            try {
                double transactionAmount = Double.parseDouble(amountText);

                if (!Double.isFinite(transactionAmount) ||
                        transactionAmount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Enter an amount greater than zero.");
                    return;
                }
                Double currentBalance = BankingContext.ACCOUNTS.checkBalance(number);

                if (currentBalance == null) {
                    showAlert(Alert.AlertType.ERROR, "Account Not Found", "No account exists with that number.");
                    return;
                }

                boolean successful;
                if ("Deposit".equalsIgnoreCase(transactionType)) {
                    successful = BankingContext.ACCOUNTS.deposit(number, transactionAmount);
                } else if ("Withdraw".equalsIgnoreCase(transactionType)) {
                    successful = BankingContext.ACCOUNTS.withdraw(number, transactionAmount);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Transaction", "Unsupported transaction type.");
                    return;
                }

                if (successful) {
                    Double newBalance = BankingContext.ACCOUNTS.checkBalance(number);

                    String balanceText = newBalance == null
                            ? "Balance temporarily unavailable"
                            : String.format("New Balance: BWP %.2f", newBalance);

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Transaction Successful",
                            transactionType +
                                    " completed successfully!\n\n" +
                                    "Account Number: " + number + "\n" +
                                    String.format("Amount: BWP %.2f%n", transactionAmount) +
                                    balanceText
                    );

                    accountNumber.clear();
                    amount.clear();

                } else {

                    showAlert(Alert.AlertType.ERROR, "Transaction Failed", "Transaction could not be completed.\n\n" + "Check the account type, available " + "balance and transaction amount.");
                }

            } catch (NumberFormatException ex) {

                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a valid numeric amount.");
            }
        });

        // BACK BUTTON
        backButton.setOnAction(e ->
                new CustomerView().show(stage)
        );

        VBox layout = new VBox(18);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(35));

        layout.getChildren().addAll(
                title,
                accountNumber,
                amount,
                submitButton,
                backButton
        );
        layout.setStyle("-fx-background-color: #F4F8FC;");
        Scene scene = new Scene(layout, 500, 450);
        stage.setTitle(transactionType + " Funds");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(
            Alert.AlertType type,
            String heading,
            String message) {

        Alert alert = new Alert(type);
        alert.setTitle(heading);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
