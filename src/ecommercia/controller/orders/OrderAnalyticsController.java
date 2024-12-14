package ecommercia.controller.orders;

import ecommercia.dao.orders.OrderDAO;
import ecommercia.model.orders.Order;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderAnalyticsController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private PieChart ordersByStatusPieChart;

    @FXML
    private BarChart<String, Number> topClientsBarChart;

    @FXML
    private CategoryAxis clientAxis;

    @FXML
    private NumberAxis totalAmountAxis;

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {
        loadAnalytics(null, null); // Initially load with no filters
    }

    /**
     * Load analytics with optional date filters.
     *
     * @param startDate Start date for filtering (nullable).
     * @param endDate   End date for filtering (nullable).
     */
    private void loadAnalytics(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderDAO.findAll();

        // Apply date filters if provided
        if (startDate != null) {
            orders = orders.stream()
                    .filter(order -> !order.getOrderDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            orders = orders.stream()
                    .filter(order -> !order.getOrderDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Update total orders and revenue
        int totalOrders = orders.size();
        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();

        totalOrdersLabel.setText(String.valueOf(totalOrders));
        totalRevenueLabel.setText(String.format("%.2f", totalRevenue));

        // Update charts
        updateOrdersByStatusChart(orders);
        updateTopClientsChart(orders); // Pass the filtered orders
    }

    /**
     * Update the pie chart displaying orders by status.
     *
     * @param orders Filtered list of orders.
     */
    private void updateOrdersByStatusChart(List<Order> orders) {
        Map<String, Long> statusData = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getStatus().getDisplayName(), Collectors.counting()));

        ordersByStatusPieChart.setData(FXCollections.observableArrayList(
                statusData.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        ));
    }

    /**
     * Update the bar chart for top clients based on the filtered orders.
     *
     * @param orders Filtered list of orders.
     */
    private void updateTopClientsChart(List<Order> orders) {
        Map<String, Double> clientRevenueData = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> orderDAO.getClientNameById(order.getClientId()), // Map client ID to name
                        Collectors.summingDouble(Order::getTotalAmount)
                ));

        topClientsBarChart.getData().clear();
        BarChart.Series<String, Number> series = new BarChart.Series<>();
        series.setName("Top Clients");

        clientRevenueData.forEach((clientName, totalRevenue) -> {
            series.getData().add(new BarChart.Data<>(clientName, totalRevenue));
        });

        topClientsBarChart.getData().add(series);
    }

    /**
     * Handle the filter button action.
     */
    @FXML
    private void handleFilter() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Reload analytics with the filtered date range
        loadAnalytics(startDate, endDate);
    }
}
