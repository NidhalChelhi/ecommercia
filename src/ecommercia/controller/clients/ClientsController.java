package ecommercia.controller.clients;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class ClientsController {

    @FXML
    private StackPane contentArea;


    @FXML
    public void initialize() {
        showIndividualClients();
    }


    @FXML
    private void showIndividualClients() {
        loadView("/ecommercia/view/clients/IndividualClientsView.fxml");
    }

    @FXML
    private void showCorporateClients() {
        loadView("/ecommercia/view/clients/CorporateClientsView.fxml");
    }

    @FXML
    private void showClientCharts() {
        loadView("/ecommercia/view/clients/ClientChartsView.fxml");
    }


    private void loadView(String fxmlPath) {
        try {
            NavigationUtil.loadContent(fxmlPath, contentArea); // Use NavigationUtil
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Failed to Load View", "Could not load the requested view. Please try again.");
        }
    }
}
