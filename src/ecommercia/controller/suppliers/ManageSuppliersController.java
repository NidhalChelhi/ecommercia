package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.SupplierDAO;
import ecommercia.model.suppliers.Supplier;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DialogUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ManageSuppliersController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Supplier> suppliersTable;

    @FXML
    private TableColumn<Supplier, String> nameColumn;

    @FXML
    private TableColumn<Supplier, String> emailColumn;

    @FXML
    private TableColumn<Supplier, String> phoneColumn;

    @FXML
    private TableColumn<Supplier, String> cityColumn;

    @FXML
    private TableColumn<Supplier, String> regionColumn;

    private final SupplierDAO supplierDAO = new SupplierDAO();
    private ObservableList<Supplier> suppliers;

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadSuppliers();

        // Add a listener to filter the suppliers based on search input
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSuppliers(newValue));
    }

    private void initializeTableColumns() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
        phoneColumn.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        cityColumn.setCellValueFactory(data -> data.getValue().cityProperty());
        regionColumn.setCellValueFactory(data -> data.getValue().regionProperty());
    }

    private void loadSuppliers() {
        List<Supplier> supplierList = supplierDAO.findAll();
        suppliers = FXCollections.observableArrayList(supplierList);
        suppliersTable.setItems(suppliers);
    }

    private void filterSuppliers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            suppliersTable.setItems(suppliers);
        } else {
            ObservableList<Supplier> filteredSuppliers = suppliers.filtered(supplier -> supplier.getName().toLowerCase().contains(searchText.toLowerCase()) || supplier.getCity().toLowerCase().contains(searchText.toLowerCase()) || supplier.getRegion().toLowerCase().contains(searchText.toLowerCase()));
            suppliersTable.setItems(filteredSuppliers);
        }
    }

    @FXML
    private void handleAddSupplier() {
        try {
            DialogUtility.showModal("/ecommercia/view/suppliers/AddSupplierPopup.fxml", "Add Supplier", controller -> ((AddSupplierController) controller).setOnSupplierAdded(this::loadSuppliers));
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Supplier dialog.");
        }
    }

    @FXML
    private void handleEditSupplier() {
        Supplier selectedSupplier = suppliersTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier != null) {
            try {
                DialogUtility.showModal("/ecommercia/view/suppliers/EditSupplierPopup.fxml", "Edit Supplier", controller -> {
                    EditSupplierController editController = (EditSupplierController) controller;
                    editController.setSupplier(selectedSupplier);
                    editController.setOnSupplierUpdated(this::loadSuppliers);
                });
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open Edit Supplier dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a supplier to edit.");
        }
    }

    @FXML
    private void handleDeleteSupplier() {
        Supplier selectedSupplier = suppliersTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier != null) {
            boolean confirmed = AlertUtility.showConfirmation("Delete Confirmation", "Are you sure you want to delete this supplier?");
            if (confirmed) {
                supplierDAO.delete(selectedSupplier.getId());
                loadSuppliers();
                AlertUtility.showInformation("Success", "Supplier deleted successfully.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a supplier to delete.");
        }
    }
}
