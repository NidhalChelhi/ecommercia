package ecommercia.controller.clients;

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

public class ClientChartsController {

    @FXML
    private PieChart clientTypePieChart;

    @FXML
    private PieChart regionPieChart;

    @FXML
    private BarChart<String, Number> ageGroupBarChart;

    @FXML
    private Label totalClientsLabel;

    @FXML
    public void initialize() {
        loadClientTypePieChart();
        loadRegionPieChart();
        loadAgeGroupBarChart();
    }

    private void loadClientTypePieChart() {
        String query = "SELECT type, COUNT(id) AS client_count FROM clients GROUP BY type";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            final int[] totalClients = {0}; // Wrap totalClients in an array to make it effectively final
            Map<String, Integer> clientTypeData = new HashMap<>();

            while (resultSet.next()) {
                String clientType = resultSet.getString("type");
                int count = resultSet.getInt("client_count");
                clientTypeData.put(clientType, count);
                totalClients[0] += count;
            }

            totalClientsLabel.setText("Total Clients: " + totalClients[0]);

            for (Map.Entry<String, Integer> entry : clientTypeData.entrySet()) {
                String clientType = entry.getKey();
                int count = entry.getValue();

                PieChart.Data data = new PieChart.Data(clientType, count);

                // Add tooltip
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        double percentage = (double) count / totalClients[0] * 100; // Use totalClients[0] from array
                        Tooltip tooltip = new Tooltip(String.format("%s: %d (%.2f%%)", clientType, count, percentage));
                        Tooltip.install(newNode, tooltip);
                    }
                });

                clientTypePieChart.getData().add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading client type pie chart data: " + e.getMessage());
        }
    }

    private void loadRegionPieChart() {
        String query = "SELECT region, COUNT(id) AS client_count FROM clients WHERE region IS NOT NULL GROUP BY region";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            Map<String, Integer> regionData = new HashMap<>();
            int totalRegions = 0;

            while (resultSet.next()) {
                String region = resultSet.getString("region");
                int count = resultSet.getInt("client_count");
                regionData.put(region, count);
                totalRegions += count;
            }

            for (Map.Entry<String, Integer> entry : regionData.entrySet()) {
                String region = entry.getKey();
                int count = entry.getValue();

                PieChart.Data data = new PieChart.Data(region, count);

                // Add tooltip
                int finalTotalRegions = totalRegions;
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        double percentage = (double) count / finalTotalRegions * 100;
                        Tooltip tooltip = new Tooltip(String.format("%s: %d (%.2f%%)", region, count, percentage));
                        Tooltip.install(newNode, tooltip);
                    }
                });

                regionPieChart.getData().add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading region pie chart data: " + e.getMessage());
        }
    }

    private void loadAgeGroupBarChart() {
        String query = "SELECT " +
                "CASE " +
                "  WHEN strftime('%Y', 'now') - strftime('%Y', date_of_birth) BETWEEN 18 AND 25 THEN '18-25' " +
                "  WHEN strftime('%Y', 'now') - strftime('%Y', date_of_birth) BETWEEN 26 AND 35 THEN '26-35' " +
                "  WHEN strftime('%Y', 'now') - strftime('%Y', date_of_birth) BETWEEN 36 AND 45 THEN '36-45' " +
                "  WHEN strftime('%Y', 'now') - strftime('%Y', date_of_birth) BETWEEN 46 AND 60 THEN '46-60' " +
                "  WHEN strftime('%Y', 'now') - strftime('%Y', date_of_birth) > 60 THEN '60+' " +
                "END AS age_group, " +
                "COUNT(id) AS client_count " +
                "FROM clients " +
                "WHERE type = 'individual' AND date_of_birth IS NOT NULL " +
                "GROUP BY age_group";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Clients by Age Group");

            while (resultSet.next()) {
                String ageGroup = resultSet.getString("age_group");
                int count = resultSet.getInt("client_count");

                if (ageGroup == null || ageGroup.isBlank()) {
                    continue; // Skip null or invalid age groups
                }

                XYChart.Data<String, Number> data = new XYChart.Data<>(ageGroup, count);

                // Add tooltip
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip tooltip = new Tooltip(String.format("%s: %d clients", ageGroup, count));
                        Tooltip.install(newNode, tooltip);
                    }
                });

                series.getData().add(data);
            }

            if (!series.getData().isEmpty()) {
                ageGroupBarChart.getData().add(series);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading age group bar chart data: " + e.getMessage());
        }
    }
}
