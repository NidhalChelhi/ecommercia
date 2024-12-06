package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.SupplierDAO;
import ecommercia.dao.suppliers.ProductSupplyDAO;
import ecommercia.model.inventory.Product;
import ecommercia.model.suppliers.Supplier;
import ecommercia.model.suppliers.ProductSupply;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddProductSupplyController {

    @FXML
    private ComboBox<Supplier> supplierComboBox;

    @FXML
    private ComboBox<Product> productComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private DatePicker supplyDatePicker;

    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ProductSupplyDAO productSupplyDAO = new ProductSupplyDAO();

    private Runnable onSupplyAdded;

    @FXML
    public void initialize() {
        // Load suppliers and products into ComboBoxes
        loadSuppliers();
        loadProducts();
    }

    public void setOnSupplyAdded(Runnable onSupplyAdded) {
        this.onSupplyAdded = onSupplyAdded;
    }

    @FXML
    private void handleAdd() {
        try {
            Supplier selectedSupplier = supplierComboBox.getValue();
            Product selectedProduct = productComboBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            LocalDate supplyDate = supplyDatePicker.getValue();

            if (selectedSupplier == null || selectedProduct == null || supplyDate == null || quantity <= 0) {
                AlertUtility.showWarning("Invalid Input", "Please fill out all fields correctly.");
                return;
            }

            ProductSupply newSupply = new ProductSupply(0, selectedSupplier.getId(), selectedProduct.getId(), quantity, supplyDate);
            productSupplyDAO.add(newSupply);

            if (onSupplyAdded != null) {
                onSupplyAdded.run();
            }

            closeWindow();
            AlertUtility.showInformation("Success", "Product supply added successfully.");
        } catch (NumberFormatException e) {
            AlertUtility.showWarning("Invalid Quantity", "Please enter a valid number for quantity.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "An error occurred while adding the supply.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) supplierComboBox.getScene().getWindow();
        stage.close();
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierDAO.findAll();
        supplierComboBox.setItems(FXCollections.observableArrayList(suppliers));
    }

    private void loadProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, name FROM products";

        try (Connection connection = DatabaseUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                products.add(new Product(id, name));
            }

            productComboBox.setItems(FXCollections.observableArrayList(products));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to load products.");
        }
    }
}
