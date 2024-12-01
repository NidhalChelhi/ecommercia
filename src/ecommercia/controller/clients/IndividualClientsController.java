package ecommercia.controller.clients;

import ecommercia.dao.ClientDAO;
import ecommercia.model.clients.IndividualClient;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DialogUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class IndividualClientsController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<IndividualClient> individualClientsTable;

    @FXML
    private TableColumn<IndividualClient, String> nameColumn;

    @FXML
    private TableColumn<IndividualClient, String> emailColumn;

    @FXML
    private TableColumn<IndividualClient, String> phoneColumn;

    @FXML
    private TableColumn<IndividualClient, String> dobColumn;

    @FXML
    private TableColumn<IndividualClient, String> cityColumn;

    @FXML
    private TableColumn<IndividualClient, String> regionColumn;

    private final ClientDAO clientDAO = new ClientDAO();
    private ObservableList<IndividualClient> clients;

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadClients();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterClients(newValue));
    }

    private void initializeTableColumns() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
        phoneColumn.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        dobColumn.setCellValueFactory(data -> data.getValue().dateOfBirthProperty().asString());
        cityColumn.setCellValueFactory(data -> data.getValue().cityProperty());
        regionColumn.setCellValueFactory(data -> data.getValue().regionProperty());
    }

    private void loadClients() {
        List<IndividualClient> clientList = clientDAO.findAllIndividualClients();
        clients = FXCollections.observableArrayList(clientList);
        individualClientsTable.setItems(clients);
    }

    private void filterClients(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            individualClientsTable.setItems(clients);
        } else {
            ObservableList<IndividualClient> filteredClients = clients.filtered(client ->
                    client.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getPhoneNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getCity().toLowerCase().contains(searchText.toLowerCase()) || // Search in City
                            client.getRegion().toLowerCase().contains(searchText.toLowerCase())   // Search in Region
            );
            individualClientsTable.setItems(filteredClients);
        }
    }

    @FXML
    private void handleAddClient() {
        try {
            DialogUtility.showModal(
                    "/ecommercia/view/clients/AddIndividualClientPopup.fxml",
                    "Add Client",
                    controller -> ((AddIndividualClientController) controller).setOnClientAdded(this::loadClients)
            );
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Client dialog.");
        }
    }

    @FXML
    private void handleEditClient() {
        IndividualClient selectedClient = individualClientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                DialogUtility.showModal(
                        "/ecommercia/view/clients/EditIndividualClientPopup.fxml",
                        "Edit Client",
                        controller -> {
                            EditIndividualClientController editController = (EditIndividualClientController) controller;
                            editController.setClient(selectedClient);
                            editController.setOnClientUpdated(this::loadClients);
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open Edit Client dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a client to edit.");
        }
    }

    @FXML
    private void handleDeleteClient() {
        IndividualClient selectedClient = individualClientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            boolean confirmed = AlertUtility.showConfirmation(
                    "Delete Confirmation", "Are you sure you want to delete this client?");
            if (confirmed) {
                clientDAO.delete(selectedClient.getId());
                loadClients();
                AlertUtility.showInformation("Success", "Client deleted successfully.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a client to delete.");
        }
    }
}
