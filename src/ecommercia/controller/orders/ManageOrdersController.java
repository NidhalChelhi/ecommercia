    package ecommercia.controller.orders;

    import ecommercia.dao.orders.OrderDAO;
    import ecommercia.model.orders.Order;
    import ecommercia.model.orders.OrderStatus;
    import ecommercia.utils.AlertUtility;
    import ecommercia.utils.DialogUtility;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;

    import java.util.List;

    public class ManageOrdersController {

        @FXML
        private TextField searchField;

        @FXML
        private TableView<Order> ordersTable;

        @FXML
        private TableColumn<Order, Integer> idColumn;

        @FXML
        private TableColumn<Order, Integer> clientIdColumn;

        @FXML
        private TableColumn<Order, String> orderDateColumn;

        @FXML
        private TableColumn<Order, OrderStatus> statusColumn;

        @FXML
        private TableColumn<Order, String> paymentMethodColumn;

        @FXML
        private TableColumn<Order, Double> totalAmountColumn;

        private final OrderDAO orderDAO = new OrderDAO();
        private ObservableList<Order> orders;

        @FXML
        public void initialize() {
            initializeTableColumns();
            loadOrders();

            // Add a listener to filter the orders based on search input
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterOrders(newValue));
        }

        private void initializeTableColumns() {
            idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
            clientIdColumn.setCellValueFactory(data -> data.getValue().clientIdProperty().asObject());
            orderDateColumn.setCellValueFactory(data -> data.getValue().orderDateProperty().asString());
            statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
            paymentMethodColumn.setCellValueFactory(data -> data.getValue().paymentMethodProperty().asString());
            totalAmountColumn.setCellValueFactory(data -> data.getValue().totalAmountProperty().asObject());
        }

        private void loadOrders() {
            List<Order> orderList = orderDAO.findAll();
            orders = FXCollections.observableArrayList(orderList);
            ordersTable.setItems(orders);
        }

        private void filterOrders(String searchText) {
            if (searchText == null || searchText.isEmpty()) {
                ordersTable.setItems(orders);
            } else {
                ObservableList<Order> filteredOrders = orders.filtered(order ->
                        String.valueOf(order.getId()).contains(searchText) ||
                                String.valueOf(order.getClientId()).contains(searchText) ||
                                order.getStatus().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                                order.getOrderDate().toString().contains(searchText)
                );
                ordersTable.setItems(filteredOrders);
            }
        }

        @FXML
        private void handleEditStatus() {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                try {
                    DialogUtility.showModal(
                            "/ecommercia/view/orders/EditOrderStatusPopup.fxml",
                            "Edit Order Status",
                            controller -> {
                                EditOrderStatusController editController = (EditOrderStatusController) controller;
                                editController.setOrder(selectedOrder);
                                editController.setOnStatusUpdated(this::loadOrders);
                            }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtility.showError("Error", "Failed to open Edit Order Status dialog.");
                }
            } else {
                AlertUtility.showWarning("No Selection", "Please select an order to edit.");
            }
        }
        @FXML
        private void handleViewItems() {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                try {
                    DialogUtility.showModal(
                            "/ecommercia/view/orders/OrderItemsPopup.fxml",
                            "Order Items",
                            controller -> ((OrderItemsPopupController) controller).setOrderId(selectedOrder.getId())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtility.showError("Error", "Failed to open Order Items dialog.");
                }
            } else {
                AlertUtility.showWarning("No Selection", "Please select an order to view items.");
            }
        }

    }
