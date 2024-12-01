package ecommercia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Ecommercia extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the LoginView.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/ecommercia/view/auth/LoginView.fxml"));

            // Create the scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Add a listener to center the stage after it's shown and fully initialized
            stage.setOnShown(event -> centerStage(stage));

            // Set the title and icon
            stage.setTitle("Ecommercia - Login");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/icon.png")));

            // Show the stage
            stage.show();

            // Add a close request event
            stage.setOnCloseRequest(event -> System.out.println("Application is closing..."));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the login screen.");
        }
    }

    /**
     * Centers the stage on the screen.
     *
     * @param stage The stage to be centered.
     */
    private void centerStage(Stage stage) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
