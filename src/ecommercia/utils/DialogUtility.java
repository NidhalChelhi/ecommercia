package ecommercia.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogUtility {

    /**
     * Opens a modal dialog and provides the controller for further configuration.
     *
     * @param fxmlPath      Path to the FXML file.
     * @param title         Title of the dialog.
     * @param parentControllerCallback Callback to configure the controller after loading.
     * @param <T>           Type of the controller.
     * @throws IOException  If the FXML cannot be loaded.
     */
    public static <T> void showModal(String fxmlPath, String title, ControllerCallback<T> parentControllerCallback) throws IOException {
        FXMLLoader loader = new FXMLLoader(DialogUtility.class.getResource(fxmlPath));
        Parent root = loader.load();

        T controller = loader.getController();
        if (parentControllerCallback != null) {
            parentControllerCallback.onControllerLoaded(controller);
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FunctionalInterface
    public interface ControllerCallback<T> {
        void onControllerLoaded(T controller);
    }
}
