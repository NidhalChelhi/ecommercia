package ecommercia.dao.suppliers;

import ecommercia.model.suppliers.ProductSupply;
import ecommercia.utils.DatabaseUtility;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ProductSupplyDAO {
    public List<ProductSupply> findAll() {
        List<ProductSupply> productSupplies = new ArrayList<>();
        String query = "SELECT ps.id, s.name AS supplier_name, p.name AS product_name, ps.quantity, ps.supply_date " +
                "FROM product_supplies ps " +
                "JOIN suppliers s ON ps.supplier_id = s.id " +
                "JOIN products p ON ps.product_id = p.id";

        try (Connection connection = DatabaseUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                productSupplies.add(new ProductSupply(
                        resultSet.getInt("id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("supply_date").toLocalDate() // Ensure correct date parsing
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productSupplies;
    }

    public void add(ProductSupply productSupply) {
        String query = "INSERT INTO product_supplies (supplier_id, product_id, quantity, supply_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productSupply.getSupplierId());
            statement.setInt(2, productSupply.getProductId());
            statement.setInt(3, productSupply.getQuantity());
            long timestamp = productSupply.getSupplyDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            statement.setLong(4, timestamp);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(ProductSupply productSupply) {
        String query = "UPDATE product_supplies SET supplier_id = ?, product_id = ?, quantity = ?, supply_date = ? WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productSupply.getSupplierId());
            statement.setInt(2, productSupply.getProductId());
            statement.setInt(3, productSupply.getQuantity());
            statement.setDate(4, Date.valueOf(productSupply.getSupplyDate()));
            statement.setInt(5, productSupply.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int supplyId) {
        String query = "DELETE FROM product_supplies WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, supplyId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
