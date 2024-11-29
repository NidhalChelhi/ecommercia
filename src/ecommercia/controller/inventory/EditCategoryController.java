package ecommercia.controller.inventory;

import ecommercia.model.inventory.Category;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditCategoryController {

    @FXML
    private TextField nameField;

    private Category category;
    private Runnable onSaveCallback;

    public void setCategory(Category category) {
        this.category = category;
        nameField.setText(category.getName());
    }

    public void handleSave() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            AlertUtility.showWarning("Invalid Input", "Category name cannot be empty.");
            return;
        }

        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, category.getId());
            statement.executeUpdate();
            closePopup();

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to edit category.");
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
