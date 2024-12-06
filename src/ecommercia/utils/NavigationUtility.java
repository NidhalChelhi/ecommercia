package ecommercia.utils;

import ecommercia.controller.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class NavigationUtility {

    public static void navigateTo(String fxmlFile, Stage stage, String title) {
        try {
            Parent root = FXMLLoader.load(NavigationUtility.class.getResource(fxmlFile));

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setTitle(title);

            centerStage(stage);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load view: " + fxmlFile);
        }
    }

    public static void navigateToWithUser(String fxmlFile, Stage stage, String title, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtility.class.getResource(fxmlFile));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserId(userId);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setTitle(title);

            centerStage(stage);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to navigate to: " + fxmlFile);
        }
    }

    private static void centerStage(Stage stage) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    public static void loadContent(String fxmlFile, Pane container) {
        try {
            Parent content = FXMLLoader.load(NavigationUtility.class.getResource(fxmlFile));
            container.getChildren().clear();
            container.getChildren().add(content);

            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

            container.applyCss();
            container.layout();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load content: " + fxmlFile);
        }
    }
}
