package ecommercia.controller.auth;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterController {

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView avatarImageView;

    private String avatarPath = null;

    @FXML
    private void handleRegister() {
        String fullname = fullnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertUtility.showWarning("Validation Error", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            AlertUtility.showWarning("Validation Error", "Invalid email format.");
            return;
        }

        if (password.length() < 6) {
            AlertUtility.showWarning("Validation Error", "Password must be at least 6 characters.");
            return;
        }

        int userId = registerUser(fullname, email, password, avatarPath);
        if (userId > 0) {
            AlertUtility.showInformation("Success", "Account created successfully!");

            // Navigate to the dashboard and pass the user ID
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            NavigationUtil.navigateToWithUser(
                    "/ecommercia/view/DashboardView.fxml",
                    currentStage,
                    "Ecommercia - Dashboard",
                    userId
            );
        }
    }

    private int registerUser(String fullname, String email, String password, String avatarPath) {
        String query = "INSERT INTO users (name, email, password, avatar) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, fullname);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, avatarPath);
            statement.executeUpdate();

            // Get the generated user ID
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                AlertUtility.showError("Registration Error", "An account with this email already exists.");
            } else {
                e.printStackTrace();
                AlertUtility.showError("Database Error", "Failed to create an account.");
            }
        }
        return -1;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    @FXML
    private void handleUploadAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            avatarPath = file.getAbsolutePath();
            avatarImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleLogin() {
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/auth/LoginView.fxml", currentStage, "Ecommercia - Login");
    }
}
