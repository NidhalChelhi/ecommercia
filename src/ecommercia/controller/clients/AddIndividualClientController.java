package ecommercia.controller.clients;

import ecommercia.dao.ClientDAO;
import ecommercia.model.clients.IndividualClient;
import ecommercia.utils.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddIndividualClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private TextField cityField;

    @FXML
    private TextField regionField;

    private ClientDAO clientDAO = new ClientDAO();

    private Runnable onClientAdded;

    public void setOnClientAdded(Runnable onClientAdded) {
        this.onClientAdded = onClientAdded;
    }

    @FXML
    private void handleAdd() {
        if (!validateInputs()) {
            return; // Stop if validation fails
        }

        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate dob = dobPicker.getValue();
        String city = cityField.getText();
        String region = regionField.getText();

        IndividualClient newClient = new IndividualClient(0, name, email, phone, null, dob, city, region);
        clientDAO.add(newClient);

        AlertUtility.showInformation("Success", "Client added successfully!");

        if (onClientAdded != null) {
            onClientAdded.run();
        }

        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    /**
     * Validates the input fields.
     *
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInputs() {
        StringBuilder errorMessages = new StringBuilder();

        if (nameField.getText().trim().length() < 3) {
            errorMessages.append("- Name must be at least 3 characters long.\n");
        }

        if (!emailField.getText().matches("\\S+@\\S+\\.\\S+")) {
            errorMessages.append("- Email must be a valid email address.\n");
        }

        if (!phoneField.getText().matches("^\\+?[0-9\\-()\\s]{7,15}$")) {
            errorMessages.append("- Phone number must be a valid format (e.g., '+216 22 222 222').\n");
        }


        if (dobPicker.getValue() == null) {
            errorMessages.append("- Date of Birth must be selected.\n");
        }

        if (cityField.getText().trim().isEmpty()) {
            errorMessages.append("- City is required.\n");
        }

        if (regionField.getText().trim().isEmpty()) {
            errorMessages.append("- Region is required.\n");
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
