package ecommercia.utils;

import ecommercia.controller.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class NavigationUtil {

    // Navigate to a new stage with a specified title
    public static void navigateTo(String fxmlFile, Stage stage, String title) {
        try {
            Parent root = FXMLLoader.load(NavigationUtil.class.getResource(fxmlFile));

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the title of the stage
            stage.setTitle(title);

            // Center the stage
            centerStage(stage);

            // Show the stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load view: " + fxmlFile);
        }
    }

    // Navigate to a new stage with a user ID and title
    public static void navigateToWithUser(String fxmlFile, Stage stage, String title, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlFile));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserId(userId);

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the title of the stage
            stage.setTitle(title);

            // Center the stage
            centerStage(stage);

            // Show the stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to navigate to: " + fxmlFile);
        }
    }

    // Centers the stage on the screen
    private static void centerStage(Stage stage) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    // Load content into a specific Pane (e.g., the center of a BorderPane)
    public static void loadContent(String fxmlFile, Pane container) {
        try {
            Parent content = FXMLLoader.load(NavigationUtil.class.getResource(fxmlFile));
            container.getChildren().clear();
            container.getChildren().add(content);

            // Bind the loaded content to the size of the container
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

            // Force layout refresh
            container.applyCss();
            container.layout();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load content: " + fxmlFile);
        }
    }
}
