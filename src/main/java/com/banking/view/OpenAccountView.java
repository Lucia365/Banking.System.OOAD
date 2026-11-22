
package com.banking.view;

import com.banking.controller.BankingContext;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;
import com.banking.model.InvestmentAccount;
import com.banking.model.ChequeAccount;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OpenAccountView {

    public void show(Stage stage) {

        Label title = new Label("OPEN BANK ACCOUNT");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );

        TextField customerId = new TextField();
        customerId.setPromptText("Customer ID");

        TextField accountNumber = new TextField();
        accountNumber.setPromptText("Account Number");

        TextField branch = new TextField();
        branch.setPromptText("Branch");

        TextField initialDeposit = new TextField();
        initialDeposit.setPromptText("Initial Deposit (BWP)");

        ComboBox<String> accountType = new ComboBox<>();
        accountType.getItems().addAll(
                "Savings Account",
                "Investment Account",
                "Cheque Account"
        );
        accountType.setPromptText("Select Account Type");
        accountType.setMaxWidth(Double.MAX_VALUE);

        // CHEQUE ACCOUNT FIELDS

        TextField employerName = new TextField();
        employerName.setPromptText("Employer / Company Name");

        TextField employerAddress = new TextField();
        employerAddress.setPromptText("Employer / Company Address");

        TextField monthlySalary = new TextField();
        monthlySalary.setPromptText("Monthly Salary (BWP)");

        TextField chequeNumber = new TextField();
        chequeNumber.setPromptText("Cheque Number");

        TextField monthlyFee = new TextField();
        monthlyFee.setPromptText("Monthly Account Fee (BWP)");

        VBox chequeFields = new VBox(
                15,
                employerName,
                employerAddress,
                monthlySalary,
                chequeNumber,
                monthlyFee
        );

        chequeFields.setVisible(false);
        chequeFields.setManaged(false);

        // SHOW CHEQUE FIELDS ONLY WHEN SELECTED

        accountType.setOnAction(e -> {

            boolean isCheque = "Cheque Account".equals(
                    accountType.getValue()
            );

            chequeFields.setVisible(isCheque);
            chequeFields.setManaged(isCheque);
        });

        // BUTTONS

        Button openButton = new Button("Open Account");
        Button backButton = new Button("Back");

        openButton.setStyle(
                "-fx-background-color: #17365D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;"
        );

        backButton.setStyle(
                "-fx-background-color: #2874A6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;"
        );

        // OPEN ACCOUNT BUTTON

        openButton.setOnAction(e -> {

            String id = customerId.getText().trim();
            String number = accountNumber.getText().trim();
            String branchName = branch.getText().trim();
            String type = accountType.getValue();
            String depositText = initialDeposit.getText().trim();

            if (id.isEmpty() ||
                    number.isEmpty() ||
                    branchName.isEmpty() ||
                    depositText.isEmpty() ||
                    type == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Missing Information",
                        "Please fill in all required fields."
                );
                return;
            }

            try {

                double deposit = Double.parseDouble(depositText);

                if (!Double.isFinite(deposit) || deposit < 0) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Invalid Deposit",
                            "Initial deposit must be a valid non-negative amount."
                    );
                    return;
                }

                // FIND REGISTERED CUSTOMER

                Customer customer =
                        BankingContext.CUSTOMERS.findCustomer(id);

                if (customer == null) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Customer Not Found",
                            "Please register this customer before opening an account."
                    );
                    return;
                }

                // CHECK DUPLICATE ACCOUNT NUMBER

                if (BankingContext.ACCOUNTS.findAccount(number) != null) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Duplicate Account",
                            "This account number already exists."
                    );
                    return;
                }

                Account account;

                switch (type) {

                    case "Savings Account":

                        account = new SavingsAccount(
                                number,
                                deposit,
                                branchName,
                                0.0005
                        );
                        break;

                    case "Investment Account":

                        if (deposit < 500) {

                            showAlert(
                                    Alert.AlertType.WARNING,
                                    "Minimum Deposit Required",
                                    "Investment accounts require at least BWP 500."
                            );
                            return;
                        }

                        account = new InvestmentAccount(
                                number,
                                deposit,
                                branchName,
                                0.05,
                                500.00
                        );
                        break;

                    case "Cheque Account":

                        String employer =
                                employerName.getText().trim();

                        String employerLocation =
                                employerAddress.getText().trim();

                        String salaryText =
                                monthlySalary.getText().trim();

                        String cheque =
                                chequeNumber.getText().trim();

                        String feeText =
                                monthlyFee.getText().trim();

                        if (employer.isEmpty() ||
                                employerLocation.isEmpty() ||
                                salaryText.isEmpty() ||
                                cheque.isEmpty() ||
                                feeText.isEmpty()) {

                            showAlert(
                                    Alert.AlertType.WARNING,
                                    "Missing Cheque Details",
                                    "Please complete all employment and cheque details."
                            );
                            return;
                        }

                        double salary =
                                Double.parseDouble(salaryText);

                        double fee =
                                Double.parseDouble(feeText);

                        if (!Double.isFinite(salary) || salary <= 0) {

                            showAlert(
                                    Alert.AlertType.ERROR,
                                    "Invalid Salary",
                                    "Monthly salary must be greater than zero."
                            );
                            return;
                        }

                        if (!Double.isFinite(fee) || fee < 0) {

                            showAlert(
                                    Alert.AlertType.ERROR,
                                    "Invalid Monthly Fee",
                                    "Monthly fee must be a valid non-negative amount."
                            );
                            return;
                        }

                        // KEEP ORIGINAL CHEQUE ACCOUNT CONSTRUCTOR

                        account = new ChequeAccount(
                                number,
                                deposit,
                                branchName,
                                cheque,
                                fee
                        );
                        break;

                    default:

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Invalid Account Type",
                                "Please select a valid account type."
                        );
                        return;
                }

                // SAVE ACCOUNT

                boolean opened;

                if (account instanceof ChequeAccount) {

                    opened = BankingContext.ACCOUNTS.openChequeAccount(
                            customer,
                            (ChequeAccount) account,
                            employerName.getText().trim(),
                            employerAddress.getText().trim(),
                            Double.parseDouble(
                                    monthlySalary.getText().trim()
                            )
                    );

                } else {

                    opened = BankingContext.ADMIN.openAccount(
                            customer,
                            account
                    );
                }

                if (opened) {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Account Created",
                            "Account opened successfully!\n\n" +
                                    "Customer ID: " + id + "\n" +
                                    "Account Number: " + number + "\n" +
                                    "Account Type: " + type + "\n" +
                                    String.format(
                                            "Opening Balance: BWP %.2f",
                                            deposit
                                    )
                    );

                    // CLEAR ALL FIELDS

                    customerId.clear();
                    accountNumber.clear();
                    branch.clear();
                    initialDeposit.clear();

                    employerName.clear();
                    employerAddress.clear();
                    monthlySalary.clear();
                    chequeNumber.clear();
                    monthlyFee.clear();

                    accountType.getSelectionModel().clearSelection();

                    chequeFields.setVisible(false);
                    chequeFields.setManaged(false);

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Account Creation Failed",
                            "Unable to open account. Check the account details."
                    );
                }

            } catch (NumberFormatException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Number",
                        "Please enter valid numeric values for deposit, salary and monthly fee."
                );
            }
        });

        // BACK BUTTON

        backButton.setOnAction(e ->
                new AdministratorView().show(stage)
        );

        // MAIN LAYOUT

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(35));

        layout.getChildren().addAll(
                title,
                customerId,
                accountNumber,
                branch,
                initialDeposit,
                accountType,
                chequeFields,
                openButton,
                backButton
        );

        layout.setStyle(
                "-fx-background-color: #F4F8FC;"
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 500, 550);

        stage.setTitle("Open Account");
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
