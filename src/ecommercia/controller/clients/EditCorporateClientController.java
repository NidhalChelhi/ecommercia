package ecommercia.controller.clients;

import ecommercia.dao.ClientDAO;
import ecommercia.model.clients.CorporateClient;
import ecommercia.utils.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCorporateClientController {

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

    private CorporateClient client;
    private final ClientDAO clientDAO = new ClientDAO();

    private Runnable onClientUpdated;

    public void setClient(CorporateClient client) {
        this.client = client;
        loadClientDetails();
    }

    public void setOnClientUpdated(Runnable onClientUpdated) {
        this.onClientUpdated = onClientUpdated;
    }

    private void loadClientDetails() {
        if (client != null) {
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            phoneField.setText(client.getPhoneNumber());
            companyNameField.setText(client.getCompanyName());
            taxIdField.setText(client.getTaxId());
            cityField.setText(client.getCity());
            regionField.setText(client.getRegion());
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) return;

        client.setName(nameField.getText());
        client.setEmail(emailField.getText());
        client.setPhoneNumber(phoneField.getText());
        client.setCompanyName(companyNameField.getText());
        client.setTaxId(taxIdField.getText());
        client.setCity(cityField.getText());
        client.setRegion(regionField.getText());

        clientDAO.update(client);

        AlertUtility.showInformation("Success", "Client updated successfully!");

        if (onClientUpdated != null) onClientUpdated.run();

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
        if (companyNameField.getText().trim().isEmpty()) errorMessages.append("- Company Name is required.\n");
        if (taxIdField.getText().trim().isEmpty()) errorMessages.append("- Tax ID is required.\n");
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
