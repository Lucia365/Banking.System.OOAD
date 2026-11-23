
package com.banking.view;

import com.banking.controller.BankingContext;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoleSelectionView {

    public void show(Stage stage) {

        Label title = new Label("BANKING SYSTEM");
        title.setStyle("-fx-font-size: 28px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #17365D;");

        Label subtitle = new Label("Welcome! Please select your role");
        Button adminButton = new Button("Bank Clerk Login");
        Button customerButton = new Button("Customer Login");

        adminButton.setPrefWidth(250);
        customerButton.setPrefWidth(250);

        adminButton.setStyle("-fx-background-color: #17365D;" + "-fx-text-fill: white;" + "-fx-font-size: 15px;");

        customerButton.setStyle("-fx-background-color: #2874A6;" + "-fx-text-fill: white;" + "-fx-font-size: 15px;");

        // ADMINISTRATOR BUTTON
        adminButton.setOnAction(e -> {

            BankingContext.LOGIN.selectRole("Administrator");

            LoginView login = new LoginView();
            login.show(stage, "Administrator");
        });

        // CUSTOMER BUTTON
        customerButton.setOnAction(e -> {

            BankingContext.LOGIN.selectRole("Customer");
            LoginView login = new LoginView();
            login.show(stage, "Customer");
        });

        VBox layout = new VBox(20);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(title, subtitle, adminButton, customerButton);

        layout.setStyle("-fx-background-color: #F4F8FC;");

        Scene scene = new Scene(layout, 500, 400);

        stage.setTitle("Banking System");
        stage.setScene(scene);
        stage.show();
    }
}
