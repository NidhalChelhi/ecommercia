package ecommercia.controller;

import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String fullname = fullnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // TODO: Save user data to database
        System.out.println("Register successful!");

        Stage currentStage = (Stage) emailField.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/DashboardView.fxml", currentStage, "Ecommercia - Dashboard");

    }


    @FXML
    private void handleLogin() {
        System.out.println("Login clicked!");
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/LoginView.fxml", currentStage, "Ecommercia - Login");
    }
}
