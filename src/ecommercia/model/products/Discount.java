package ecommercia.model.products;

public class Discount {
    private int id;
    private Product product; // Association with Product
    private double percentage; // Discount percentage (e.g., 20.0 for 20%)

    public Discount(int id, Product product, double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        this.id = id;
        this.product = product;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        this.percentage = percentage;
    }

    public double calculateDiscountedPrice() {
        return product.getPrice() * (1 - percentage / 100);
    }

    @Override
    public String toString() {
        return "Discount{" +
                "product=" + product.getName() +
                ", percentage=" + percentage +
                '}';
    }
}
