
package com.banking.view;

import com.banking.controller.BankingContext;
import com.banking.model.Customer;
import com.banking.model.IndividualCustomer;
import com.banking.model.CompanyCustomer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RegisterCustomerView {

    public void show(Stage stage) {

        Label title = new Label("REGISTER CUSTOMER");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField customerId = new TextField();
        customerId.setPromptText("Customer ID");

        TextField firstName = new TextField();
        firstName.setPromptText("First Name");

        TextField surname = new TextField();
        surname.setPromptText("Surname");

        TextField address = new TextField();
        address.setPromptText("Address");

        ComboBox<String> customerType = new ComboBox<>();
        customerType.getItems().addAll(
                "Individual Customer",
                "Company Customer"
        );
        customerType.setPromptText("Select Customer Type");
        customerType.setMaxWidth(Double.MAX_VALUE);

        // Individual customer fields
        TextField nationalId = new TextField();
        nationalId.setPromptText("National ID");

        DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setPromptText("Date of Birth");
        dateOfBirth.setMaxWidth(Double.MAX_VALUE);

        // Company customer fields
        TextField companyName = new TextField();
        companyName.setPromptText("Company Name");

        TextField companyAddress = new TextField();
        companyAddress.setPromptText("Company Address");

        TextField registrationNumber = new TextField();
        registrationNumber.setPromptText(
                "Company Registration Number"
        );

        VBox individualFields = new VBox(
                14, nationalId, dateOfBirth
        );

        VBox companyFields = new VBox(
                14,
                companyName,
                companyAddress,
                registrationNumber
        );

        individualFields.setVisible(false);
        individualFields.setManaged(false);

        companyFields.setVisible(false);
        companyFields.setManaged(false);

        // Show fields based on customer type
        customerType.setOnAction(e -> {

            boolean individual = "Individual Customer".equals(
                    customerType.getValue()
            );

            boolean company = "Company Customer".equals(
                    customerType.getValue()
            );

            individualFields.setVisible(individual);
            individualFields.setManaged(individual);

            companyFields.setVisible(company);
            companyFields.setManaged(company);
        });

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        // REGISTER BUTTON
        registerButton.setOnAction(e -> {

            String id = customerId.getText().trim();
            String name = firstName.getText().trim();
            String lastName = surname.getText().trim();
            String customerAddress = address.getText().trim();
            String type = customerType.getValue();

            if (id.isEmpty() ||
                    name.isEmpty() ||
                    lastName.isEmpty() ||
                    customerAddress.isEmpty() ||
                    type == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Missing Information",
                        "Please fill in all customer details."
                );
                return;
            }

            try {

                Customer customer;

                if (type.equals("Individual Customer")) {

                    String nationalIdText =
                            nationalId.getText().trim();

                    if (nationalIdText.isEmpty() ||
                            dateOfBirth.getValue() == null) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Missing Information",
                                "Enter National ID and Date of Birth."
                        );
                        return;
                    }

                    int idNumber =
                            Integer.parseInt(nationalIdText);

                    if (idNumber <= 0 ||
                            dateOfBirth.getValue().isAfter(
                                    java.time.LocalDate.now())) {
                        throw new IllegalArgumentException(
                                "Invalid National ID or date of birth."
                        );
                    }

                    customer = new IndividualCustomer(
                            id,
                            name,
                            lastName,
                            customerAddress,
                            new ArrayList<>(),
                            type,
                            idNumber,
                            dateOfBirth.getValue().toString()
                    );

                } else if (type.equals("Company Customer")) {

                    String cName =
                            companyName.getText().trim();

                    String cAddress =
                            companyAddress.getText().trim();

                    String regText =
                            registrationNumber.getText().trim();

                    if (cName.isEmpty() ||
                            cAddress.isEmpty() ||
                            regText.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Missing Information",
                                "Please enter all company details."
                        );
                        return;
                    }

                    int regNumber =
                            Integer.parseInt(regText);

                    if (regNumber <= 0) {
                        throw new IllegalArgumentException(
                                "Registration number must be positive."
                        );
                    }

                    customer = new CompanyCustomer(
                            id,
                            name,
                            lastName,
                            customerAddress,
                            new ArrayList<>(),
                            type,
                            cName,
                            cAddress,
                            regNumber
                    );

                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Invalid Type",
                            "Please select a valid customer type."
                    );
                    return;
                }

                // Save using the shared controller
                boolean registered =
                        BankingContext.CUSTOMERS
                                .registerCustomer(customer);

                if (registered) {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Registration Successful",
                            "Customer " + name + " " +
                                    lastName +
                                    " registered successfully!"
                    );

                    // Clear fields after successful registration
                    customerId.clear();
                    firstName.clear();
                    surname.clear();
                    address.clear();

                    nationalId.clear();
                    dateOfBirth.setValue(null);

                    companyName.clear();
                    companyAddress.clear();
                    registrationNumber.clear();

                    customerType.getSelectionModel()
                            .clearSelection();

                    individualFields.setVisible(false);
                    individualFields.setManaged(false);

                    companyFields.setVisible(false);
                    companyFields.setManaged(false);

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Registration Failed",
                            "Customer ID already exists or is invalid."
                    );
                }

            } catch (NumberFormatException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Number",
                        "National ID and registration number must contain only digits within the supported range."
                );

            } catch (IllegalArgumentException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Information",
                        ex.getMessage()
                );
            }
        });

        // BACK BUTTON
        backButton.setOnAction(e ->
                new AdministratorView().show(stage)
        );

        VBox layout = new VBox(14);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(35));

        layout.getChildren().addAll(
                title,
                customerId,
                firstName,
                surname,
                address,
                customerType,
                individualFields,
                companyFields,
                registerButton,
                backButton
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: #F4F8FC;"
        );

        layout.setStyle(
                "-fx-background-color: #F4F8FC;"
        );

        Scene scene = new Scene(scrollPane, 500, 550);

        stage.setTitle("Register Customer");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
