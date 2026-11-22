
package org.example;

import com.banking.view.RoleSelectionView;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        RoleSelectionView roleView =
                new RoleSelectionView();

        roleView.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
