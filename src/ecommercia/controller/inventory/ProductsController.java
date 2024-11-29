package ecommercia.controller.inventory;

import ecommercia.model.inventory.Category;
import ecommercia.model.inventory.Product;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.DialogUtility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    private ObservableList<Product> products;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        categoryColumn.setCellValueFactory(data -> {
            Category category = data.getValue().getCategory();
            return new ReadOnlyStringWrapper(category != null ? category.getName() : "No Category");
        });

        stockColumn.setCellValueFactory(data -> data.getValue().stockProperty().asObject());

        loadProducts();
    }

    private void loadProducts() {
        products = FXCollections.observableArrayList();
        String query = """
                    SELECT p.id AS product_id,
                           p.name AS product_name,
                           p.price,
                           p.stock,
                           c.id AS category_id,
                           c.name AS category_name
                    FROM products p
                    LEFT JOIN categories c ON p.category_id = c.id
                """;

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = null;

                // Handle cases where category_id is NULL
                if (resultSet.getInt("category_id") != 0) {
                    category = new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    );
                }

                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        category,
                        resultSet.getInt("stock")
                );

                products.add(product);
            }

            productsTable.setItems(products);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load products.");
        }
    }


    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Product> filteredProducts = products.filtered(product -> {
            boolean matchesName = product.getName().toLowerCase().contains(searchText);
            boolean matchesCategory = product.getCategory() != null
                    && product.getCategory().getName().toLowerCase().contains(searchText);
            return matchesName || matchesCategory;
        });
        productsTable.setItems(filteredProducts);
    }


    @FXML
    private void handleAddProduct() {
        try {
            DialogUtility.showModal(
                    "/ecommercia/view/inventory/AddProductPopup.fxml",
                    "Add Product",
                    controller -> ((AddProductController) controller).setOnProductAdded(unused -> loadProducts())
            );
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Product dialog.");
        }
    }

    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                DialogUtility.showModal(
                        "/ecommercia/view/inventory/EditProductPopup.fxml",
                        "Edit Product",
                        controller -> {
                            EditProductController editController = (EditProductController) controller;
                            editController.setProduct(selectedProduct);
                            editController.setOnProductUpdated(unused -> loadProducts());
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open Edit Product dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a product to edit.");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (AlertUtility.showConfirmation("Delete Product", "Are you sure you want to delete this product?")) {
                deleteProduct(selectedProduct.getId());
                products.remove(selectedProduct);
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a product to delete.");
        }
    }

    private void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to delete the product.");
        }
    }
}
