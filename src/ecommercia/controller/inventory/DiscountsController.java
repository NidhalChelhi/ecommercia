package ecommercia.controller.inventory;

import ecommercia.model.inventory.Discount;
import ecommercia.model.inventory.Product;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.DialogUtility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscountsController {

    @FXML
    private TableView<Discount> discountsTable;

    @FXML
    private TableColumn<Discount, String> productColumn;

    @FXML
    private TableColumn<Discount, Double> percentageColumn;

    @FXML
    private TableColumn<Discount, String> finalPriceColumn;

    private ObservableList<Discount> discounts;

    @FXML
    public void initialize() {
        productColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getProduct().getName()));
        percentageColumn.setCellValueFactory(data -> data.getValue().percentageProperty().asObject());
        finalPriceColumn.setCellValueFactory(data -> {
            double finalPrice = data.getValue().calculateDiscountedPrice();
            return new ReadOnlyStringWrapper(String.format("%.2f", finalPrice));
        });

        loadDiscounts();
    }

    public void loadDiscounts() {
        discounts = FXCollections.observableArrayList();
        String query = """
                SELECT d.percentage,
                       p.name AS product_name, p.price, p.id AS product_id
                FROM discounts d
                JOIN products p ON d.product_id = p.id
                """;

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        null,
                        0
                );

                Discount discount = new Discount(product, resultSet.getDouble("percentage"));
                discounts.add(discount);
            }

            discountsTable.setItems(discounts);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load discounts from the database.");
        }
    }

    @FXML
    private void handleAddDiscount() {
        try {
            DialogUtility.showModal(
                    "/ecommercia/view/inventory/AddDiscountPopup.fxml",
                    "Add Discount",
                    (AddDiscountController controller) -> controller.setOnDiscountAdded(this::loadDiscounts)
            );
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open Add Discount dialog.");
        }
    }

    @FXML
    private void handleEditDiscount() {
        Discount selectedDiscount = discountsTable.getSelectionModel().getSelectedItem();
        if (selectedDiscount != null) {
            try {
                DialogUtility.showModal(
                        "/ecommercia/view/inventory/EditDiscountPopup.fxml",
                        "Edit Discount",
                        (EditDiscountController controller) -> {
                            controller.setDiscount(selectedDiscount);
                            controller.setOnDiscountUpdated(this::loadDiscounts);
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open Edit Discount dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a discount to edit.");
        }
    }

    @FXML
    private void handleDeleteDiscount() {
        Discount selectedDiscount = discountsTable.getSelectionModel().getSelectedItem();
        if (selectedDiscount != null) {
            if (AlertUtility.showConfirmation("Delete Discount", "Are you sure you want to delete this discount?")) {
                deleteDiscount(selectedDiscount.getProduct().getId());
                discounts.remove(selectedDiscount);
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a discount to delete.");
        }
    }

    private void deleteDiscount(int productId) {
        String query = "DELETE FROM discounts WHERE product_id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to delete the discount.");
        }
    }
}
