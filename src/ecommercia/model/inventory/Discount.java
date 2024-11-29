package ecommercia.model.inventory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Discount {
    private final DoubleProperty percentage;
    private final Product product;

    public Discount(Product product, double percentage) {
        this.product = product;
        this.percentage = new SimpleDoubleProperty(percentage);
    }

    public Product getProduct() {
        return product;
    }

    public double getPercentage() {
        return percentage.get();
    }

    public void setPercentage(double percentage) {
        this.percentage.set(percentage);
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }

    public double calculateDiscountedPrice() {
        return product.getPrice() * (1 - percentage.get() / 100);
    }

}
