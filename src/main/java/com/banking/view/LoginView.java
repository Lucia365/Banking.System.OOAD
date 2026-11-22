
package com.banking.view;

import com.banking.controller.BankingContext;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private String selectedRole;

    public void show(Stage stage, String role) {

        selectedRole = role;

        // Ensure the login controller knows the selected role
        if (!BankingContext.LOGIN.selectRole(role)) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Invalid user role."
            ).showAndWait();
            return;
        }

        Label title = new Label(role + " Login");

        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );

        // Username field
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(270);

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(270);

        // Login button
        loginButton = new Button("Login");
        loginButton.setPrefWidth(270);

        loginButton.setStyle(
                "-fx-background-color: #17365D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;"
        );

        // LOGIN BUTTON FUNCTIONALITY
        loginButton.setOnAction(e -> {

            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            // Validate empty fields
            if (username.isEmpty() || password.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText("Login Failed");
                alert.setContentText(
                        "Please enter your username and password."
                );
                alert.showAndWait();
                return;
            }

            // Authenticate through LoginController
            boolean valid =
                    BankingContext.LOGIN.authenticate(
                            username,
                            password
                    );

            if (valid) {

                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION
                );

                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Welcome to the Banking System!"
                );
                alert.showAndWait();

                // Open the correct dashboard
                if (selectedRole.equalsIgnoreCase(
                        "Administrator")) {

                    new AdministratorView().show(stage);

                } else if (selectedRole.equalsIgnoreCase(
                        "Customer")) {

                    new CustomerView().show(stage);
                }

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText(
                        "Incorrect username or password."
                );
                alert.showAndWait();
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setPrefWidth(270);

        backButton.setOnAction(e -> {

            BankingContext.LOGIN.logout();

            RoleSelectionView roleView =
                    new RoleSelectionView();

            roleView.show(stage);
        });

        // Layout
        VBox layout = new VBox(18);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
                title,
                usernameField,
                passwordField,
                loginButton,
                backButton
        );

        layout.setStyle(
                "-fx-background-color: #F4F8FC;"
        );

        // Scene
        Scene scene = new Scene(layout, 450, 400);

        stage.setTitle("Banking System - " + role);
        stage.setScene(scene);
        stage.show();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
