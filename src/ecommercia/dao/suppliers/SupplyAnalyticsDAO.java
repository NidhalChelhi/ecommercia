package ecommercia.dao.suppliers;

import ecommercia.model.suppliers.SupplyAnalytics;
import ecommercia.utils.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplyAnalyticsDAO {

    public List<SupplyAnalytics> getTopSuppliers() {
        List<SupplyAnalytics> analytics = new ArrayList<>();
        String query = "SELECT suppliers.name AS supplier_name, products.name AS product_name, SUM(product_supplies.quantity) AS total_supplies " +
                "FROM product_supplies " +
                "JOIN suppliers ON product_supplies.supplier_id = suppliers.id " +
                "JOIN products ON product_supplies.product_id = products.id " +
                "GROUP BY supplier_name, product_name " +
                "ORDER BY total_supplies DESC";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                analytics.add(new SupplyAnalytics(
                        resultSet.getString("supplier_name"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("total_supplies")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return analytics;
    }
}
