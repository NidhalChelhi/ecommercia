package ecommercia.controller;

import ecommercia.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private AnchorPane contentArea;

    public void initialize() {
        // Load the products view by default
        NavigationUtil.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    @FXML
    private void showProductsPage() {
        NavigationUtil.loadContent("/ecommercia/view/inventory/InventoryView.fxml", contentArea);
    }

    @FXML
    private void showClientsPage() {
        NavigationUtil.loadContent("/ecommercia/view/ClientsView.fxml", contentArea);
    }

    @FXML
    private void showOrdersPage() {
        NavigationUtil.loadContent("/ecommercia/view/OrdersView.fxml", contentArea);
    }

    @FXML
    private void showSuppliersPage() {
        NavigationUtil.loadContent("/ecommercia/view/SuppliersView.fxml", contentArea);
    }

    @FXML
    private void handleLogout() {
        Stage currentStage = (Stage) contentArea.getScene().getWindow();
        NavigationUtil.navigateTo("/ecommercia/view/auth/LoginView.fxml", currentStage, "Ecommercia - Login");
    }
}
