package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.ProductSupplyDAO;
import ecommercia.model.suppliers.ProductSupply;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DialogUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ProductSuppliesController {

    @FXML
    private TextField supplierFilterField;

    @FXML
    private TextField productFilterField;

    @FXML
    private TableView<ProductSupply> suppliesTable;

    @FXML
    private TableColumn<ProductSupply, String> supplierNameColumn;

    @FXML
    private TableColumn<ProductSupply, String> productNameColumn;

    @FXML
    private TableColumn<ProductSupply, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductSupply, String> supplyDateColumn;

    private final ProductSupplyDAO productSupplyDAO = new ProductSupplyDAO();
    private ObservableList<ProductSupply> supplies;

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadSupplies();

        // Add listeners for search filters
        supplierFilterField.textProperty().addListener((observable, oldValue, newValue) -> filterSupplies());
        productFilterField.textProperty().addListener((observable, oldValue, newValue) -> filterSupplies());
    }

    private void initializeTableColumns() {
        supplierNameColumn.setCellValueFactory(data -> data.getValue().supplierNameProperty());
        productNameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        supplyDateColumn.setCellValueFactory(data -> data.getValue().supplyDateProperty().asString());
    }

    private void loadSupplies() {
        List<ProductSupply> supplyList = productSupplyDAO.findAll();
        supplies = FXCollections.observableArrayList(supplyList);
        suppliesTable.setItems(supplies);
    }

    private void filterSupplies() {
        String supplierFilter = supplierFilterField.getText().toLowerCase();
        String productFilter = productFilterField.getText().toLowerCase();

        if (supplierFilter.isEmpty() && productFilter.isEmpty()) {
            suppliesTable.setItems(supplies);
            return;
        }

        ObservableList<ProductSupply> filteredSupplies = supplies.filtered(supply ->
                supply.getSupplierName().toLowerCase().contains(supplierFilter) &&
                        supply.getProductName().toLowerCase().contains(productFilter)
        );

        suppliesTable.setItems(filteredSupplies);
    }

    @FXML
    private void handleAddSupply() {
        try {
            DialogUtility.showModal(
                    "/ecommercia/view/suppliers/AddProductSupplyPopup.fxml",
                    "Add Product Supply",
                    controller -> ((AddProductSupplyController) controller).setOnSupplyAdded(this::loadSupplies)
            );
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Supply dialog.");
        }
    }


    @FXML
    private void handleDeleteSupply() {
        ProductSupply selectedSupply = suppliesTable.getSelectionModel().getSelectedItem();
        if (selectedSupply != null) {
            boolean confirmed = AlertUtility.showConfirmation(
                    "Delete Confirmation", "Are you sure you want to delete this supply?");
            if (confirmed) {
                productSupplyDAO.delete(selectedSupply.getId());
                loadSupplies();
                AlertUtility.showInformation("Success", "Supply deleted successfully.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a supply to delete.");
        }
    }
}
