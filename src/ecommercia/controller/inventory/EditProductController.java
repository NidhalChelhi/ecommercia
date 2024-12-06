package ecommercia.controller.inventory;

import ecommercia.model.inventory.Category;
import ecommercia.model.inventory.Product;
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

public class EditProductController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField stockField;

    private Product product;
    private ObservableList<Category> categories;

    private Consumer<Void> onProductUpdated;

    public void setProduct(Product product) {
        this.product = product;
        loadProductDetails();
    }

    public void setOnProductUpdated(Consumer<Void> onProductUpdated) {
        this.onProductUpdated = onProductUpdated;
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

    private void loadProductDetails() {
        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getStock()));
            categoryComboBox.setValue(product.getCategory());
        }
    }

    @FXML
    private void handleSave() {
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

            String query = "UPDATE products SET name = ?, price = ?, category_id = ?, stock = ? WHERE id = ?";
            try (Connection connection = DatabaseUtility.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setInt(3, selectedCategory.getId());
                statement.setInt(4, stock);
                statement.setInt(5, product.getId());
                statement.executeUpdate();

                AlertUtility.showInformation("Success", "Product updated successfully!");

                if (onProductUpdated != null) {
                    onProductUpdated.accept(null);
                }

                closeDialog();
            }
        } catch (NumberFormatException e) {
            AlertUtility.showError("Validation Error", "Price and Stock must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to update product.");
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
