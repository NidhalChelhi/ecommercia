package ecommercia.controller.inventory;

import ecommercia.model.inventory.Category;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

public class AddProductController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField stockField;

    private ObservableList<Category> categories;

    private Consumer<Void> onProductAdded; // Callback for notifying ProductsController

    public void setOnProductAdded(Consumer<Void> onProductAdded) {
        this.onProductAdded = onProductAdded;
    }

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        categories = FXCollections.observableArrayList();
        String query = "SELECT id, name FROM categories";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            categoryComboBox.setItems(categories);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load categories.");
        }
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        Category selectedCategory = categoryComboBox.getValue();
        String stockText = stockField.getText();

        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null || stockText.isEmpty()) {
            AlertUtility.showWarning("Validation Error", "All fields are required!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            String query = "INSERT INTO products (name, price, category_id, stock) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseUtility.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setInt(3, selectedCategory.getId());
                statement.setInt(4, stock);
                statement.executeUpdate();

                AlertUtility.showInformation("Success", "Product added successfully!");

                // Notify the ProductsController to refresh the product list
                if (onProductAdded != null) {
                    onProductAdded.accept(null);
                }

                closeDialog();
            }
        } catch (NumberFormatException e) {
            AlertUtility.showError("Validation Error", "Price and Stock must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to add product.");
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
