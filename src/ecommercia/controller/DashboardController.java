package ecommercia.controller;

import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private ImageView userAvatar;

    @FXML
    private Text userName;

    private int userId; // Store the ID of the logged-in user

    @FXML
    public void initialize() {
        // Apply circular clipping to the avatar
        makeImageCircular(userAvatar);

        // Load the products view by default
        NavigationUtil.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    /**
     * Sets the logged-in user's ID.
     *
     * @param userId The ID of the logged-in user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
        loadUserProfile();
    }

    /**
     * Loads the user's profile information and updates the sidebar.
     */
    private void loadUserProfile() {
        String query = "SELECT name, avatar FROM users WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userName.setText(resultSet.getString("name"));

                String avatarPath = resultSet.getString("avatar");
                if (isValidImage(avatarPath)) {
                    userAvatar.setImage(new Image("file:" + avatarPath));
                } else {
                    userAvatar.setImage(new Image("/resources/images/icon.png"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load user profile.");
        }
    }

    /**
     * Applies a circular clip to the given ImageView.
     *
     * @param imageView The ImageView to be clipped.
     */
    private void makeImageCircular(ImageView imageView) {
        Circle circle = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
        imageView.setClip(circle);
    }

    /**
     * Validates if the provided file path points to a valid image.
     *
     * @param filePath Path to the file to validate.
     * @return true if the file is a valid image, false otherwise.
     */
    private boolean isValidImage(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;
        File file = new File(filePath);
        return file.exists() && (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".jpeg"));
    }

    @FXML
    private void showProductsPage() {
        NavigationUtil.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    @FXML
    private void showClientsPage() {
        NavigationUtil.loadContent("/ecommercia/view/clients/ClientsView.fxml", contentArea);
    }

    @FXML
    private void showOrdersPage() {
        NavigationUtil.loadContent("/ecommercia/view/OrdersView.fxml", contentArea);
    }

    @FXML
    private void showSuppliersPage() {
        NavigationUtil.loadContent("/ecommercia/view/SuppliersView.fxml", contentArea);
    }

    @FXML
    private void handleLogout() {
        Stage currentStage = (Stage) contentArea.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/auth/LoginView.fxml", currentStage, "Ecommercia - Login");
    }
}
