package ecommercia.controller.inventory;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCategoryController {

    @FXML
    private TextField nameField;

    private Runnable onSaveCallback;

    public void handleSave() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            AlertUtility.showWarning("Invalid Input", "Category name cannot be empty.");
            return;
        }

        String query = "INSERT INTO categories (name) VALUES (?)";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.executeUpdate();
            closePopup();

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to add category.");
        }
    }

    public void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }
}
