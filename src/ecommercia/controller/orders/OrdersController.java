package ecommercia.controller.orders;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.NavigationUtility;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class OrdersController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showManageOrders();
    }

    @FXML
    public void showManageOrders() {
        loadView("/ecommercia/view/orders/ManageOrdersView.fxml");
    }

    @FXML
    public void showOrderAnalytics() {
        loadView("/ecommercia/view/orders/OrderAnalyticsView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            NavigationUtility.loadContent(fxmlPath, contentArea); // Use NavigationUtil
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Failed to Load View", "Could not load the requested view. Please try again.");
        }
    }

}
