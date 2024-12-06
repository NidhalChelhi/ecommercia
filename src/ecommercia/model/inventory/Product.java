package ecommercia.model.inventory;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final ObjectProperty<Category> category;
    private final IntegerProperty stock;

    public Product(int id, String name, double price, Category category, int stock) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleObjectProperty<>(category);
        this.stock = new SimpleIntegerProperty(stock);
    }

    public Product(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(0.0);
        this.category = new SimpleObjectProperty<>(null);
        this.stock = new SimpleIntegerProperty(0);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public Category getCategory() {
        return category.get();
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }

    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    @Override
    public String toString() {
        return getName();
    }
}
