
package com.banking.view;

import com.banking.controller.BankingContext;
import com.banking.model.Customer;
import com.banking.model.Account;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdministratorView {

    public void show(Stage stage) {

        Label title = new Label("ADMINISTRATOR");
        title.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );

        Label subtitle = new Label("Banking System Management");

        Button registerButton = new Button("Register Customer");

        Button openAccountButton = new Button("Open Account");

        Button viewCustomersButton = new Button("View Customers");

        Button transactionsButton = new Button("Transaction History");

        Button logoutButton = new Button("Logout");

        Button[] buttons = {
                registerButton,
                openAccountButton,
                viewCustomersButton,
                transactionsButton,
                logoutButton
        };

        for (Button button : buttons) {

            button.setPrefWidth(280);
            button.setPrefHeight(42);

            button.setStyle(
                    "-fx-background-color: #17365D;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 8;"
            );
        }

        // 1. REGISTER CUSTOMER
        registerButton.setOnAction(e ->
                new RegisterCustomerView().show(stage)
        );

        // 2. OPEN ACCOUNT
        openAccountButton.setOnAction(e ->
                new OpenAccountView().show(stage)
        );

        // 3. VIEW CUSTOMERS
        viewCustomersButton.setOnAction(e -> {

            List<Customer> customers =
                    BankingContext.ADMIN.viewCustomers();

            StringBuilder details = new StringBuilder();

            if (customers.isEmpty()) {
                details.append(
                        "No customers have been registered yet."
                );
            } else {
                for (Customer customer : customers) {
                    details.append("Customer ID: ")
                            .append(customer.getCustomerId())
                            .append("\n");

                    details.append("First Name: ")
                            .append(customer.getFirstName())
                            .append("\n");

                    details.append("Surname: ")
                            .append(customer.getSurname())
                            .append("\n");

                    details.append("Address: ")
                            .append(customer.getAddress())
                            .append("\n");

                    details.append("----------------------\n");
                }
            }

            showDetails(
                    "Registered Customers",
                    details.toString()
            );
        });

        // 4. TRANSACTION HISTORY
        transactionsButton.setOnAction(e -> {

            List<Account> accounts =
                    BankingContext.ADMIN.viewAccounts();

            StringBuilder details = new StringBuilder();

            if (accounts.isEmpty()) {

                details.append(
                        "No accounts have been opened yet."
                );

            } else {

                details.append(
                        "Registered Account Summary\n\n"
                );

                for (Account account : accounts) {

                    details.append("Account Number: ")
                            .append(account.getAccountNumber())
                            .append("\n");

                    details.append("Balance: BWP ")
                            .append(String.format(
                                    "%.2f",
                                    account.getBalance()
                            ))
                            .append("\n");

                    details.append("----------------------\n");
                }
            }

            showDetails(
                    "Transaction History",
                    details.toString()
            );
        });

        // 5. LOGOUT
        logoutButton.setOnAction(e -> {

            BankingContext.LOGIN.logout();

            new RoleSelectionView().show(stage);
        });

        VBox layout = new VBox(16);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(title, subtitle, registerButton, openAccountButton, viewCustomersButton, transactionsButton, logoutButton);

        layout.setStyle("-fx-background-color: #F4F8FC;");
        Scene scene = new Scene(layout, 600, 550);
        stage.setTitle("Banking System - Administrator");
        stage.setScene(scene);
        stage.show();
    }

    // Display information in a scrollable dialog
    private void showDetails(
            String heading,
            String information) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle(heading);
        dialog.getDialogPane().getButtonTypes().add(
                ButtonType.CLOSE
        );

        TextArea textArea = new TextArea(information);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(450, 350);

        dialog.getDialogPane().setContent(textArea);

        dialog.showAndWait();
    }
}
