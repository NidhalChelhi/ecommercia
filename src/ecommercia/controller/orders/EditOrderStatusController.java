package ecommercia.controller.orders;

import ecommercia.dao.orders.OrderDAO;
import ecommercia.model.orders.Order;
import ecommercia.model.orders.OrderStatus;
import ecommercia.utils.AlertUtility;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EditOrderStatusController {

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label currentStatusLabel;

    @FXML
    private ComboBox<OrderStatus> statusComboBox;

    private OrderDAO orderDAO = new OrderDAO();
    private Order order;
    private Runnable onStatusUpdated;

    public void setOrder(Order order) {
        this.order = order;

        // Set current order details
        orderIdLabel.setText(String.valueOf(order.getId()));
        currentStatusLabel.setText(order.getStatus().toString());
        statusComboBox.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        statusComboBox.setValue(order.getStatus());
    }

    public void setOnStatusUpdated(Runnable onStatusUpdated) {
        this.onStatusUpdated = onStatusUpdated;
    }

    @FXML
    private void handleUpdate() {
        OrderStatus newStatus = statusComboBox.getValue();
        if (newStatus == null || newStatus == order.getStatus()) {
            AlertUtility.showWarning("Invalid Status", "Please select a different status to update.");
            return;
        }

        try {
            // Update the order status in the database
            order.setStatus(newStatus);
            orderDAO.update(order);

            // Notify the caller about the update
            if (onStatusUpdated != null) {
                onStatusUpdated.run();
            }

            // Close the window
            closeWindow();
            AlertUtility.showInformation("Success", "Order status updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to update order status.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) orderIdLabel.getScene().getWindow();
        stage.close();
    }
}
