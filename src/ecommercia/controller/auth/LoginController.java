package ecommercia.controller.auth;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

//        if (validateCredentials(email, password)) {
        if (true) {
            System.out.println("Login successful!");

            // Navigate to the dashboard
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            NavigationUtil.navigateTo("/ecommercia/view/DashboardView.fxml", currentStage, "Ecommercia - Dashboard");
        } else {
            AlertUtility.showWarning("Login Failed", "Invalid email or password. Please try again.");
        }
    }

    private boolean validateCredentials(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if a match is found
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "An error occurred while validating credentials.");
            return false;
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
        NavigationUtil.navigateTo("/ecommercia/view/auth/RegisterView.fxml", currentStage, "Ecommercia - Register");
    }
}
