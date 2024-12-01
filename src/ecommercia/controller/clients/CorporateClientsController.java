package ecommercia.controller.clients;

import ecommercia.dao.ClientDAO;
import ecommercia.model.clients.CorporateClient;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DialogUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CorporateClientsController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<CorporateClient> corporateClientsTable;

    @FXML
    private TableColumn<CorporateClient, String> nameColumn;

    @FXML
    private TableColumn<CorporateClient, String> emailColumn;

    @FXML
    private TableColumn<CorporateClient, String> phoneColumn;

    @FXML
    private TableColumn<CorporateClient, String> companyColumn;

    @FXML
    private TableColumn<CorporateClient, String> taxColumn;

    @FXML
    private TableColumn<CorporateClient, String> cityColumn;

    @FXML
    private TableColumn<CorporateClient, String> regionColumn;

    private final ClientDAO clientDAO = new ClientDAO();
    private ObservableList<CorporateClient> clients;

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
        companyColumn.setCellValueFactory(data -> data.getValue().companyNameProperty());
        taxColumn.setCellValueFactory(data -> data.getValue().taxIdProperty());
        cityColumn.setCellValueFactory(data -> data.getValue().cityProperty());
        regionColumn.setCellValueFactory(data -> data.getValue().regionProperty());
    }

    private void loadClients() {
        List<CorporateClient> clientList = clientDAO.findAllCorporateClients();
        clients = FXCollections.observableArrayList(clientList);
        corporateClientsTable.setItems(clients);
    }

    private void filterClients(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            corporateClientsTable.setItems(clients);
        } else {
            ObservableList<CorporateClient> filteredClients = clients.filtered(client ->
                    client.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getPhoneNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getCompanyName().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getTaxId().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getCity().toLowerCase().contains(searchText.toLowerCase()) ||
                            client.getRegion().toLowerCase().contains(searchText.toLowerCase())
            );
            corporateClientsTable.setItems(filteredClients);
        }
    }

    @FXML
    private void handleAddClient() {
        try {
            DialogUtility.showModal(
                    "/ecommercia/view/clients/AddCorporateClientPopup.fxml",
                    "Add Client",
                    controller -> ((AddCorporateClientController) controller).setOnClientAdded(this::loadClients)
            );
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Client dialog.");
        }
    }

    @FXML
    private void handleEditClient() {
        CorporateClient selectedClient = corporateClientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                DialogUtility.showModal(
                        "/ecommercia/view/clients/EditCorporateClientPopup.fxml",
                        "Edit Client",
                        controller -> {
                            EditCorporateClientController editController = (EditCorporateClientController) controller;
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
        CorporateClient selectedClient = corporateClientsTable.getSelectionModel().getSelectedItem();
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
