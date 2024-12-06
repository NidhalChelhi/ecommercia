package ecommercia.controller.inventory;

import ecommercia.model.inventory.Discount;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditDiscountController {

    @FXML
    private Label productNameLabel;

    @FXML
    private TextField percentageField;

    @FXML
    private Label finalPriceLabel;

    private Discount discount;

    private Runnable onDiscountUpdated;

    public void setDiscount(Discount discount) {
        this.discount = discount;
        productNameLabel.setText(discount.getProduct().getName());
        percentageField.setText(String.valueOf(discount.getPercentage()));
        updateFinalPrice();
    }

    public void setOnDiscountUpdated(Runnable onDiscountUpdated) {
        this.onDiscountUpdated = onDiscountUpdated;
    }

    @FXML
    public void initialize() {
        percentageField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double percentage = Double.parseDouble(newValue);
                if (percentage >= 0 && percentage <= 100) {
                    discount.setPercentage(percentage);
                    updateFinalPrice();
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                finalPriceLabel.setText("Invalid percentage");
            }
        });
    }

    private void updateFinalPrice() {
        double finalPrice = discount.calculateDiscountedPrice();
        finalPriceLabel.setText(String.format("%.2f TND", finalPrice));
    }

    @FXML
    private void handleUpdate() {
        String percentageText = percentageField.getText();

        if (percentageText.isEmpty()) {
            AlertUtility.showWarning("Invalid Input", "Percentage cannot be empty.");
            return;
        }

        try {
            double percentage = Double.parseDouble(percentageText);

            if (percentage < 0 || percentage > 100) {
                throw new NumberFormatException();
            }

            if (percentage == 0) {
                deleteDiscountFromDatabase(discount.getProduct().getId());
                if (onDiscountUpdated != null) {
                    onDiscountUpdated.run();
                }
                closeWindow();
                return;
            }

            updateDiscountInDatabase(discount.getProduct().getId(), percentage);

            if (onDiscountUpdated != null) {
                onDiscountUpdated.run();
            }

            closeWindow();

        } catch (NumberFormatException e) {
            AlertUtility.showWarning("Invalid Input", "Percentage must be a number between 0 and 100.");
        }
    }

    private void updateDiscountInDatabase(int productId, double percentage) {
        String query = "UPDATE discounts SET percentage = ? WHERE product_id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, percentage);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to update discount.");
        }
    }

    private void deleteDiscountFromDatabase(int productId) {
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

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) percentageField.getScene().getWindow();
        stage.close();
    }
}
