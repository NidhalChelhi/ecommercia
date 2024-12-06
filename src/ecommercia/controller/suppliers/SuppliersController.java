package ecommercia.controller.suppliers;

import ecommercia.utils.NavigationUtility;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SuppliersController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showManageSuppliers();
    }

    @FXML
    public void showManageSuppliers() {
        NavigationUtility.loadContent("/ecommercia/view/suppliers/ManageSuppliersView.fxml", contentArea);
    }

    @FXML
    public void showManageProductSupplies() {
        NavigationUtility.loadContent("/ecommercia/view/suppliers/ManageProductSuppliesView.fxml", contentArea);
    }

    @FXML
    public void showSupplyAnalytics() {
        NavigationUtility.loadContent("/ecommercia/view/suppliers/SupplyAnalyticsView.fxml", contentArea);
    }
}
