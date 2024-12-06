package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.SupplierDAO;
import ecommercia.model.suppliers.Supplier;
import ecommercia.utils.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddSupplierController {

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

    private Runnable onSupplierAdded;

    public void setOnSupplierAdded(Runnable onSupplierAdded) {
        this.onSupplierAdded = onSupplierAdded;
    }

    @FXML
    private void handleAdd() {
        if (!validateInputs()) {
            return; // Stop if validation fails
        }

        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String city = cityField.getText();
        String region = regionField.getText();

        Supplier newSupplier = new Supplier(0, name, email, phone, city, region);
        supplierDAO.add(newSupplier);

        AlertUtility.showInformation("Success", "Supplier added successfully!");

        if (onSupplierAdded != null) {
            onSupplierAdded.run();
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
            errorMessages.append("- Phone must be valid.\n");
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
