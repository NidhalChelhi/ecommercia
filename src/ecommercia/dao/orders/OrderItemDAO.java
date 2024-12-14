package ecommercia.dao.orders;

import ecommercia.model.orders.OrderItem;
import ecommercia.utils.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT oi.id, p.name AS product_name, oi.quantity, oi.unit_price, " +
                "(oi.quantity * oi.unit_price) AS total_price " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                items.add(new OrderItem(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("total_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
