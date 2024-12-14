package ecommercia.controller.orders;

import ecommercia.dao.orders.OrderItemDAO;
import ecommercia.model.orders.OrderItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class OrderItemsPopupController {

    @FXML
    private TableView<OrderItem> orderItemsTable;

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, Double> unitPriceColumn;

    @FXML
    private TableColumn<OrderItem, Double> totalPriceColumn;

    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    @FXML
    public void initialize() {
        initializeTableColumns();
    }

    private void initializeTableColumns() {
        productNameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        unitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty().asObject());
        totalPriceColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty().asObject());
    }

    public void setOrderId(int orderId) {
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        orderItemsTable.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) orderItemsTable.getScene().getWindow();
        stage.close();
    }
}
