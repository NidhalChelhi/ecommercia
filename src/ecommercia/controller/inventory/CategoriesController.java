package ecommercia.controller.inventory;

import ecommercia.model.inventory.Category;
import ecommercia.utils.AlertUtility;
import ecommercia.utils.DatabaseUtility;
import ecommercia.utils.DialogUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoriesController {

    @FXML
    private TableView<Category> categoriesTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    private ObservableList<Category> categories;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        loadCategories();
    }

    public void loadCategories() {
        categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categories";

        try (Connection connection = DatabaseUtility.getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt("id"), resultSet.getString("name"));
                categories.add(category);
            }

            categoriesTable.setItems(categories);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to load categories.");
        }
    }

    @FXML
    private void handleAddCategory() {
        try {
            DialogUtility.showModal("/ecommercia/view/inventory/AddCategoryPopup.fxml", "Add Category", (AddCategoryController controller) -> controller.setOnSaveCallback(this::loadCategories));
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Error", "Failed to open the Add Category dialog.");
        }
    }

    @FXML
    private void handleEditCategory() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                DialogUtility.showModal("/ecommercia/view/inventory/EditCategoryPopup.fxml", "Edit Category", (EditCategoryController controller) -> {
                    controller.setCategory(selectedCategory);
                    controller.setOnSaveCallback(this::loadCategories);
                });
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtility.showError("Error", "Failed to open the Edit Category dialog.");
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a category to edit.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            if (AlertUtility.showConfirmation("Delete Category", "Are you sure you want to delete this category?")) {
                deleteCategoryFromDatabase(selectedCategory.getId());
                loadCategories();
            }
        } else {
            AlertUtility.showWarning("No Selection", "Please select a category to delete.");
        }
    }

    private void deleteCategoryFromDatabase(int id) {
        String query = "DELETE FROM categories WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtility.showError("Database Error", "Failed to delete category.");
        }
    }
}
