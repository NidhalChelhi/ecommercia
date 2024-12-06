package ecommercia.controller.suppliers;

import ecommercia.dao.suppliers.ProductSupplyDAO;
import ecommercia.model.suppliers.ProductSupply;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplyAnalyticsController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalSuppliesLabel;

    @FXML
    private Label totalQuantityLabel;

    @FXML
    private BarChart<String, Number> supplierBarChart;

    @FXML
    private CategoryAxis supplierAxis;

    @FXML
    private NumberAxis quantityAxis;

    @FXML
    private PieChart productPieChart;

    private final ProductSupplyDAO productSupplyDAO = new ProductSupplyDAO();

    @FXML
    public void initialize() {
        loadAnalytics();
    }

    private void loadAnalytics() {
        // Fetch all supplies
        List<ProductSupply> supplies = productSupplyDAO.findAll();

        // Calculate totals
        int totalSupplies = supplies.size();
        int totalQuantity = supplies.stream().mapToInt(ProductSupply::getQuantity).sum();

        // Update UI
        totalSuppliesLabel.setText(String.valueOf(totalSupplies));
        totalQuantityLabel.setText(String.valueOf(totalQuantity));

        // Update charts
        updateSupplierBarChart(supplies);
        updateProductPieChart(supplies);
    }

    private void updateSupplierBarChart(List<ProductSupply> supplies) {
        Map<String, Integer> supplierData = supplies.stream().collect(Collectors.groupingBy(ProductSupply::getSupplierName, Collectors.summingInt(ProductSupply::getQuantity)));

        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        supplierData.forEach((supplier, quantity) -> series.getData().add(new XYChart.Data<>(supplier, quantity)));

        barChartData.add(series);
        supplierBarChart.setData(barChartData);
    }

    private void updateProductPieChart(List<ProductSupply> supplies) {
        Map<String, Integer> productData = supplies.stream().collect(Collectors.groupingBy(ProductSupply::getProductName, Collectors.summingInt(ProductSupply::getQuantity)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        productData.forEach((product, quantity) -> pieChartData.add(new PieChart.Data(product, quantity)));

        productPieChart.setData(pieChartData);
    }

    @FXML
    private void handleFilter() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        List<ProductSupply> supplies = productSupplyDAO.findAll();

        if (startDate != null) {
            supplies = supplies.stream()
                    .filter(supply -> !supply.getSupplyDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            supplies = supplies.stream()
                    .filter(supply -> !supply.getSupplyDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Update totals and charts with filtered data
        updateUIWithFilteredData(supplies);
    }

    private void updateUIWithFilteredData(List<ProductSupply> filteredSupplies) {
        // Calculate totals
        int totalSupplies = filteredSupplies.size();
        int totalQuantity = filteredSupplies.stream().mapToInt(ProductSupply::getQuantity).sum();

        // Update UI labels
        totalSuppliesLabel.setText(String.valueOf(totalSupplies));
        totalQuantityLabel.setText(String.valueOf(totalQuantity));

        // Update charts
        updateSupplierBarChart(filteredSupplies);
        updateProductPieChart(filteredSupplies);
    }

}
