package ecommercia.model.products;

public class Inventory {
    private Product product; // Association with Product
    private int stock; // Current stock level

    public Inventory(Product product, int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.product = product;
        this.stock = stock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Insufficient stock.");
        }
        stock -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative.");
        }
        stock += quantity;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "product=" + product.getName() +
                ", stock=" + stock +
                '}';
    }
}
