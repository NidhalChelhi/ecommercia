package ecommercia.model.suppliers;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ProductSupply {
    private final IntegerProperty id;
    private final IntegerProperty supplierId;
    private final IntegerProperty productId;
    private final StringProperty supplierName;
    private final StringProperty productName;
    private final IntegerProperty quantity;
    private final ObjectProperty<LocalDate> supplyDate;

    public ProductSupply(int id, int supplierId, int productId, int quantity, LocalDate supplyDate) {
        this.id = new SimpleIntegerProperty(id);
        this.supplierId = new SimpleIntegerProperty(supplierId);
        this.productId = new SimpleIntegerProperty(productId);
        this.supplierName = new SimpleStringProperty(""); // Initialize with empty strings
        this.productName = new SimpleStringProperty("");
        this.quantity = new SimpleIntegerProperty(quantity);
        this.supplyDate = new SimpleObjectProperty<>(supplyDate);
    }

    public ProductSupply(int id, String supplierName, String productName, int quantity, LocalDate supplyDate) {
        this.id = new SimpleIntegerProperty(id);
        this.supplierId = new SimpleIntegerProperty(0); // Default value for supplierId
        this.productId = new SimpleIntegerProperty(0);  // Default value for productId
        this.supplierName = new SimpleStringProperty(supplierName);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.supplyDate = new SimpleObjectProperty<>(supplyDate);
    }

    // Getters and Property Methods
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getSupplierId() {
        return supplierId.get();
    }

    public IntegerProperty supplierIdProperty() {
        return supplierId;
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public StringProperty supplierNameProperty() {
        return supplierName;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public LocalDate getSupplyDate() {
        return supplyDate.get();
    }

    public ObjectProperty<LocalDate> supplyDateProperty() {
        return supplyDate;
    }

    // Setters (if needed)
    public void setId(int id) {
        this.id.set(id);
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setSupplyDate(LocalDate supplyDate) {
        this.supplyDate.set(supplyDate);
    }
}
