package ecommercia.controller.clients;

import ecommercia.dao.ClientDAO;
import ecommercia.model.clients.CorporateClient;
import ecommercia.utils.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCorporateClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField taxIdField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField regionField;

    private final ClientDAO clientDAO = new ClientDAO();

    private Runnable onClientAdded;

    public void setOnClientAdded(Runnable onClientAdded) {
        this.onClientAdded = onClientAdded;
    }

    @FXML
    private void handleAdd() {
        if (!validateInputs()) return;

        CorporateClient newClient = new CorporateClient(
                0,
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                null,
                companyNameField.getText(),
                taxIdField.getText(),
                cityField.getText(),
                regionField.getText()
        );

        clientDAO.add(newClient);
        AlertUtility.showInformation("Success", "Client added successfully!");

        if (onClientAdded != null) onClientAdded.run();
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        StringBuilder errorMessages = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) errorMessages.append("- Name is required.\n");
        if (!emailField.getText().matches("\\S+@\\S+\\.\\S+")) errorMessages.append("- Invalid email.\n");
        if (phoneField.getText().trim().isEmpty()) errorMessages.append("- Phone number is required.\n");
        if (cityField.getText().trim().isEmpty()) errorMessages.append("- City is required.\n");
        if (regionField.getText().trim().isEmpty()) errorMessages.append("- Region is required.\n");

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
