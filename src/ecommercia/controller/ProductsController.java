package ecommercia.controller;

import ecommercia.model.products.Category;
import ecommercia.model.products.Discount;
import ecommercia.model.products.Product;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductsController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn; // Updated to show discounted prices

    @FXML
    private TableColumn<Product, String> discountColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    private ObservableList<Product> products;

    @FXML
    public void initialize() {
        // Initialize the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Configure priceColumn as Double for proper sorting
        priceColumn.setCellValueFactory(product -> {
            double price = product.getValue().getDiscount() != null
                    ? product.getValue().getDiscount().calculateDiscountedPrice()
                    : product.getValue().getPrice();
            return new ReadOnlyObjectWrapper<>(price); // Double for sorting
        });

        // Configure custom display for priceColumn
        priceColumn.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", price));
                }
            }
        });

        // Configure discountColumn to display discount percentage
        discountColumn.setCellValueFactory(product -> {
            if (product.getValue().getDiscount() != null) {
                return new ReadOnlyStringWrapper(String.format("%.0f%%", product.getValue().getDiscount().getPercentage()));
            }
            return new ReadOnlyStringWrapper("None");
        });

        // Configure the category column
        categoryColumn.setCellValueFactory(product ->
                new ReadOnlyStringWrapper(product.getValue().getCategory().getName()));

        // Configure the stock column
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Load data from the database
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        products = FXCollections.observableArrayList();
        String query = """
            SELECT p.id AS product_id, p.name AS product_name, p.price, p.stock, 
                   c.id AS category_id, c.name AS category_name,
                   d.percentage AS discount_percentage
            FROM products p
            JOIN categories c ON p.category_id = c.id
            LEFT JOIN discounts d ON p.id = d.product_id
        """;

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt("category_id"), resultSet.getString("category_name"));
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        category,
                        resultSet.getInt("stock")
                );

                double discountPercentage = resultSet.getDouble("discount_percentage");
                if (!resultSet.wasNull()) {
                    Discount discount = new Discount(product.getId(), product, discountPercentage);
                    product.setDiscount(discount);
                }

                products.add(product);
            }
            productsTable.setItems(products);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load products from the database.");
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Product> filteredProducts = products.filtered(product ->
                product.getName().toLowerCase().contains(query) ||
                        product.getCategory().getName().toLowerCase().contains(query)
        );
        productsTable.setItems(filteredProducts);
    }

    @FXML
    private void handleAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ecommercia/view/AddProductView.fxml"));
            Parent parent = loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Product");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(parent));
            dialogStage.showAndWait();

            // Reload products after adding
            loadProductsFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open the Add Product dialog.");
        }
    }

    @FXML
    private void handleApplyDiscount() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Apply Discount");
            dialog.setHeaderText("Apply a discount to: " + selectedProduct.getName());
            dialog.setContentText("Enter discount percentage:");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    double percentage = Double.parseDouble(input);
                    if (percentage >= 0 && percentage <= 100) {
                        applyDiscountToProduct(selectedProduct, percentage);
                        AlertUtility.showInformation("Success", "Discount applied successfully.");
                        loadProductsFromDatabase();
                    } else {
                        AlertUtility.showWarning("Invalid Input", "Percentage must be between 0 and 100.");
                    }
                } catch (NumberFormatException e) {
                    AlertUtility.showError("Invalid Input", "Please enter a valid number.");
                }
            });
        } else {
            AlertUtility.showWarning("No Selection", "Please select a product to apply a discount.");
        }
    }

    private void applyDiscountToProduct(Product product, double percentage) {
        String query = "INSERT INTO discounts (product_id, percentage) VALUES (?, ?) ON CONFLICT(product_id) DO UPDATE SET percentage = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, product.getId());
            statement.setDouble(2, percentage);
            statement.setDouble(3, percentage);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to apply discount.");
        }
    }

    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ecommercia/view/EditProductView.fxml"));
                Parent parent = loader.load();

                // Pass selected product to the EditProductController
                EditProductController controller = loader.getController();
                controller.setProduct(selectedProduct);

                // Show the dialog
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Product");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setScene(new Scene(parent));
                dialogStage.showAndWait();

                // Reload products after editing
                loadProductsFromDatabase();
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open the Edit Product dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a product to edit.");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Product");
            confirmationAlert.setContentText("Are you sure you want to delete the product: " + selectedProduct.getName() + "?");

            if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                deleteProductFromDatabase(selectedProduct.getId());
                products.remove(selectedProduct);
                AlertUtility.showInformation("Product Deleted", "The product has been successfully deleted.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a product to delete.");
        }
    }

    private void deleteProductFromDatabase(int productId) {
        String query = "DELETE FROM products WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to delete product from the database.");
        }
    }
}
