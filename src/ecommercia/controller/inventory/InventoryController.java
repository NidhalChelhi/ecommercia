package ecommercia.controller.inventory;

import ecommercia.utils.AlertUtility;
import ecommercia.utils.NavigationUtility;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class InventoryController {

    @FXML
    private StackPane contentArea;


    @FXML
    public void initialize() {
        showProductsList();
    }


    @FXML
    private void showProductsList() {
        loadView("/ecommercia/view/inventory/ProductsView.fxml");
    }

    @FXML
    private void showCategories() {
        loadView("/ecommercia/view/inventory/CategoriesView.fxml");
    }

    @FXML
    private void showDiscounts() {
        loadView("/ecommercia/view/inventory/DiscountsView.fxml");
    }

    @FXML
    private void showCharts() {
        loadView("/ecommercia/view/inventory/ChartsView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            NavigationUtility.loadContent(fxmlPath, contentArea);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Failed to Load View", "Could not load the requested view. Please try again.");
        }
    }
}
