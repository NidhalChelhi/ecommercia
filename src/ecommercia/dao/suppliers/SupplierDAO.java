package ecommercia.dao.suppliers;

import ecommercia.model.suppliers.Supplier;
import ecommercia.utils.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers";

        try (Connection connection = DatabaseUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                suppliers.add(new Supplier(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("city"),
                        resultSet.getString("region")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    public void add(Supplier supplier) {
        String query = "INSERT INTO suppliers (name, email, phone_number, city, region) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getCity());
            statement.setString(5, supplier.getRegion());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Supplier supplier) {
        String query = "UPDATE suppliers SET name = ?, email = ?, phone_number = ?, city = ?, region = ? WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getCity());
            statement.setString(5, supplier.getRegion());
            statement.setInt(6, supplier.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int supplierId) {
        String query = "DELETE FROM suppliers WHERE id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, supplierId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
