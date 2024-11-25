package ecommercia.controller;

import ecommercia.model.products.Category;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddProductController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Category> categoryComboBox; // Updated to use ComboBox

    @FXML
    private TextField stockField;

    private ObservableList<Category> categories;

    @FXML
    public void initialize() {
        loadCategories(); // Populate categoryComboBox with categories from the database
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
        Category selectedCategory = categoryComboBox.getValue(); // Get the selected category
        String stockText = stockField.getText();

        // Validation
        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null || stockText.isEmpty()) {
            AlertUtility.showWarning("Validation Error", "All fields are required!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            // Add product to the database
            String query = "INSERT INTO products (name, price, category_id, stock) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseUtility.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setInt(3, selectedCategory.getId()); // Use category ID
                statement.setInt(4, stock);
                statement.executeUpdate();

                System.out.println("Product added successfully!");

                // Close the dialog
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e) {
            AlertUtility.showError("Validation Error", "Price and Stock must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to add product to the database.");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
