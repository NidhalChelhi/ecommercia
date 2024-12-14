package ecommercia.dao.orders;

import ecommercia.model.orders.Order;
import ecommercia.model.orders.OrderStatus;
import ecommercia.model.orders.PaymentMethod;
import ecommercia.utils.DatabaseUtility;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";

        try (Connection connection = DatabaseUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Read the date as a string and parse it
                String dateString = resultSet.getString("order_date");
                LocalDate orderDate = LocalDate.parse(dateString, DATE_FORMATTER);

                orders.add(new Order(
                        resultSet.getInt("id"),
                        resultSet.getInt("client_id"),
                        orderDate,
                        OrderStatus.fromDatabaseValue(resultSet.getString("status")),
                        PaymentMethod.fromDatabaseValue(resultSet.getString("payment_method")),
                        resultSet.getDouble("total_amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public void update(Order order) {
        String query = "UPDATE orders SET client_id = ?, order_date = ?, status = ?, payment_method = ?, total_amount = ? WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, order.getClientId());
            statement.setString(2, order.getOrderDate().format(DATE_FORMATTER));
            statement.setString(3, order.getStatus().name());
            statement.setString(4, order.getPaymentMethod().name());
            statement.setDouble(5, order.getTotalAmount());
            statement.setInt(6, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int orderId) {
        String query = "DELETE FROM orders WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch total revenue grouped by client name.
     *
     * @return A map of client names to total revenue.
     */
    public Map<String, Double> getRevenueByClient() {
        Map<String, Double> revenueByClient = new HashMap<>();
        String query = """
                SELECT c.name AS client_name, SUM(o.total_amount) AS total_revenue
                FROM orders o
                JOIN clients c ON o.client_id = c.id
                GROUP BY c.name
                ORDER BY total_revenue DESC
                LIMIT 10
                """;

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String clientName = resultSet.getString("client_name");
                double totalRevenue = resultSet.getDouble("total_revenue");
                revenueByClient.put(clientName, totalRevenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueByClient;
    }

    public String getClientNameById(int clientId) {
        String query = "SELECT name FROM clients WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Client";
    }

}
