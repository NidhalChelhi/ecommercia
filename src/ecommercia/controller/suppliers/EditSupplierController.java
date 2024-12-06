package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.SupplierDAO;
import ecommercia.model.suppliers.Supplier;
import ecommercia.utils.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditSupplierController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField regionField;

    private final SupplierDAO supplierDAO = new SupplierDAO();

    private Supplier supplier;

    private Runnable onSupplierUpdated;

    /**
     * Sets the supplier to be edited.
     *
     * @param supplier The supplier object to edit.
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        loadSupplierDetails();
    }

    /**
     * Sets the callback to be executed when the supplier is successfully updated.
     *
     * @param onSupplierUpdated The callback to run.
     */
    public void setOnSupplierUpdated(Runnable onSupplierUpdated) {
        this.onSupplierUpdated = onSupplierUpdated;
    }

    private void loadSupplierDetails() {
        if (supplier != null) {
            nameField.setText(supplier.getName());
            emailField.setText(supplier.getEmail());
            phoneField.setText(supplier.getPhoneNumber());
            cityField.setText(supplier.getCity());
            regionField.setText(supplier.getRegion());
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return; // Stop execution if validation fails
        }

        supplier.setName(nameField.getText());
        supplier.setEmail(emailField.getText());
        supplier.setPhoneNumber(phoneField.getText());
        supplier.setCity(cityField.getText());
        supplier.setRegion(regionField.getText());

        supplierDAO.update(supplier);

        AlertUtility.showInformation("Success", "Supplier updated successfully!");

        if (onSupplierUpdated != null) {
            onSupplierUpdated.run();
        }

        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        StringBuilder errorMessages = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errorMessages.append("- Name cannot be empty.\n");
        }

        if (emailField.getText().trim().isEmpty() || !emailField.getText().matches("\\S+@\\S+\\.\\S+")) {
            errorMessages.append("- Email must be valid.\n");
        }

        if (phoneField.getText().trim().isEmpty() || !phoneField.getText().matches("^\\+?[0-9\\-()\\s]{7,15}$")) {
            errorMessages.append("- Phone number must be valid.\n");
        }

        if (cityField.getText().trim().isEmpty()) {
            errorMessages.append("- City cannot be empty.\n");
        }

        if (regionField.getText().trim().isEmpty()) {
            errorMessages.append("- Region cannot be empty.\n");
        }

        if (errorMessages.length() > 0) {
            AlertUtility.showWarning("Validation Error", errorMessages.toString());
            return false;
        }

        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
