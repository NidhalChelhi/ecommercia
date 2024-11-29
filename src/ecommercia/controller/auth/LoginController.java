package ecommercia.controller.auth;

import ecommercia.controller.DashboardController;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        int userId = validateCredentials(email, password);
        if (userId != -1) {
            System.out.println("Login successful!");

            // Navigate to the dashboard with the user's ID
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            NavigationUtil.navigateToWithUser(
                    "/ecommercia/view/DashboardView.fxml",
                    currentStage,
                    "Ecommercia - Dashboard",
                    userId
            );
        } else {
            AlertUtility.showWarning("Login Failed", "Invalid email or password. Please try again.");
        }
    }

    private int validateCredentials(String email, String password) {
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id"); // Return the user's ID if credentials are valid
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "An error occurred while validating credentials.");
        }
        return -1; // Return -1 if credentials are invalid
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
