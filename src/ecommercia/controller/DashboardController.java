package ecommercia.controller;

import ecommercia.utils.AudioPlaybackUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.NavigationUtility;
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

    private int userId;

    @FXML
    public void initialize() {
        makeImageCircular(userAvatar);

        NavigationUtility.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadUserProfile();
    }

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

    private void makeImageCircular(ImageView imageView) {
        Circle circle = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
        imageView.setClip(circle);
    }

    private boolean isValidImage(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;
        File file = new File(filePath);
        return file.exists() && (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".jpeg"));
    }

    @FXML
    private void showProductsPage() {
        NavigationUtility.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    @FXML
    private void showClientsPage() {
        NavigationUtility.loadContent("/ecommercia/view/clients/ClientsView.fxml", contentArea);
    }

    @FXML
    private void showOrdersPage() {
        NavigationUtility.loadContent("/ecommercia/view/orders/OrdersView.fxml", contentArea);
    }

    @FXML
    private void showSuppliersPage() {
        NavigationUtility.loadContent("/ecommercia/view/suppliers/SuppliersView.fxml", contentArea);
    }

    @FXML
    private void handleLogout() {
        AudioPlaybackUtility audioPlaybackUtility = new AudioPlaybackUtility();
        audioPlaybackUtility.playGoodbyeAudioAsync();
        Stage currentStage = (Stage) contentArea.getScene().getWindow();
        NavigationUtility.navigateTo("/ecommercia/view/auth/LoginView.fxml", currentStage, "Ecommercia - Login");
    }
}
