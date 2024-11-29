package ecommercia.controller.inventory;

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

public class AddDiscountController {

    @FXML
    private ComboBox<Product> productComboBox;

    @FXML
    private TextField percentageField;

    private Runnable onDiscountAdded;

    public void setOnDiscountAdded(Runnable onDiscountAdded) {
        this.onDiscountAdded = onDiscountAdded;
    }

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = """
                SELECT p.id, p.name, p.price
                FROM products p
                WHERE p.id NOT IN (SELECT product_id FROM discounts)
                """;

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        null,
                        0
                ));
            }

            productComboBox.setItems(products);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load products.");
        }
    }

    @FXML
    private void handleAdd() {
        Product selectedProduct = productComboBox.getValue();
        String percentageText = percentageField.getText();

        if (selectedProduct == null || percentageText.isEmpty()) {
            AlertUtility.showWarning("Invalid Input", "Please fill in all fields.");
            return;
        }

        try {
            double percentage = Double.parseDouble(percentageText);

            // Validate percentage range
            if (percentage < 0 || percentage > 100) {
                throw new NumberFormatException();
            }

            // No need to add a discount if percentage is 0
            if (percentage == 0) {
                AlertUtility.showInformation("No Discount Added", "A discount with 0% is not created.");
                closeWindow();
                return;
            }

            addDiscountToDatabase(selectedProduct.getId(), percentage);

            if (onDiscountAdded != null) {
                onDiscountAdded.run();
            }

            closeWindow();

        } catch (NumberFormatException e) {
            AlertUtility.showWarning("Invalid Input", "Percentage must be a number between 0 and 100.");
        }
    }

    private void addDiscountToDatabase(int productId, double percentage) {
        String query = "INSERT INTO discounts (product_id, percentage) VALUES (?, ?)";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            statement.setDouble(2, percentage);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to add discount.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) productComboBox.getScene().getWindow();
        stage.close();
    }
}
