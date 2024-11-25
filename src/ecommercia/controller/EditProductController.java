package ecommercia.controller;

import ecommercia.model.products.Category;
import ecommercia.model.products.Product;
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

public class EditProductController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Category> categoryComboBox; // Updated to use ComboBox

    @FXML
    private TextField stockField;

    private Product product;
    private ObservableList<Category> categories;

    public void setProduct(Product product) {
        this.product = product;

        // Pre-fill fields
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        categoryComboBox.setValue(product.getCategory()); // Set the current category
    }

    @FXML
    public void initialize() {
        loadCategories(); // Populate categoryComboBox
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
    private void handleSave() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        Category selectedCategory = categoryComboBox.getValue();
        String stockText = stockField.getText();

        // Validation
        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null || stockText.isEmpty()) {
            AlertUtility.showWarning("Validation Error", "All fields are required.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            // Update product in database
            String query = "UPDATE products SET name = ?, price = ?, category_id = ?, stock = ? WHERE id = ?";
            try (Connection connection = DatabaseUtility.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setInt(3, selectedCategory.getId()); // Use category ID
                statement.setInt(4, stock);
                statement.setInt(5, product.getId());
                statement.executeUpdate();

                System.out.println("Product updated successfully!");

                // Close the dialog
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e) {
            AlertUtility.showError("Validation Error", "Price and Stock must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to update the product.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
