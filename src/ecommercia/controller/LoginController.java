package ecommercia.controller;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();


//        if ("admin@example.com".equals(email) && "password".equals(password)) {

        if (true) {
            System.out.println("Login successful!");

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            NavigationUtil.navigateTo("/ecommercia/view/DashboardView.fxml", currentStage, "Ecommercia - Dashboard");
        } else {
            AlertUtility.showWarning("Login Failed", "Invalid email or password. Please try again.");
        }
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot Password clicked!");
        AlertUtility.showInformation("Forgot Password", "Password recovery functionality is not implemented yet.");
        // TODO: Add navigation or logic for password recovery
    }

    @FXML
    private void handleSignUp() {
        System.out.println("Sign Up clicked!");
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/RegisterView.fxml", currentStage, "Ecommercia - Register");
    }
}
