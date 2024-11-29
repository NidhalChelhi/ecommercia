package ecommercia.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtility {

    public static void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showWarning(String title, String content) {
        showAlert(title, content, Alert.AlertType.WARNING);
    }

    public static void showInformation(String title, String content) {
        showAlert(title, content, Alert.AlertType.INFORMATION);
    }

    public static void showError(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

}
