package ecommercia.controller.inventory;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import ecommercia.utils.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ChartsController {

    @FXML
    private PieChart categoryPieChart;

    @FXML
    private BarChart<String, Number> discountBarChart;

    @FXML
    private Label totalProductsLabel;

    @FXML
    private Label totalDiscountsLabel;

    @FXML
    public void initialize() {
        loadCategoryPieChart();
        loadDiscountBarChart();
    }

    private void loadCategoryPieChart() {
        String query = "SELECT c.name AS category, COUNT(p.id) AS product_count " +
                "FROM categories c " +
                "LEFT JOIN products p ON c.id = p.category_id " +
                "GROUP BY c.name";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            int[] totalProducts = {0}; // Use an array to make it effectively final
            Map<String, Integer> categoryData = new HashMap<>();

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int productCount = resultSet.getInt("product_count");
                categoryData.put(category, productCount);
                totalProducts[0] += productCount;
            }

            totalProductsLabel.setText("Total Products: " + totalProducts[0]);

            for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
                String category = entry.getKey();
                int productCount = entry.getValue();

                PieChart.Data data = new PieChart.Data(category, productCount);

                // Use totalProducts[0] inside the lambda
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        double percentage = (double) productCount / totalProducts[0] * 100;
                        Tooltip tooltip = new Tooltip(String.format("%s: %d (%.2f%%)", category, productCount, percentage));
                        Tooltip.install(newNode, tooltip);
                    }
                });

                categoryPieChart.getData().add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading category pie chart data: " + e.getMessage());
        }
    }


    private void loadDiscountBarChart() {
        String query = "SELECT " +
                "CASE " +
                "  WHEN percentage BETWEEN 0 AND 10 THEN '0-10%' " +
                "  WHEN percentage BETWEEN 11 AND 20 THEN '11-20%' " +
                "  WHEN percentage BETWEEN 21 AND 30 THEN '21-30%' " +
                "  WHEN percentage > 30 THEN '30%+' " +
                "END AS discount_range, " +
                "COUNT(id) AS product_count " +
                "FROM discounts " +
                "GROUP BY discount_range";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            int totalDiscounts = 0;
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Products by Discount Range");

            while (resultSet.next()) {
                String discountRange = resultSet.getString("discount_range");
                int productCount = resultSet.getInt("product_count");
                totalDiscounts += productCount;

                XYChart.Data<String, Number> data = new XYChart.Data<>(discountRange, productCount);

                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip tooltip = new Tooltip(String.format("%s: %d products", discountRange, productCount));
                        Tooltip.install(newNode, tooltip);
                    }
                });

                series.getData().add(data);
            }

            totalDiscountsLabel.setText("Total Discounts Applied: " + totalDiscounts);
            discountBarChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
