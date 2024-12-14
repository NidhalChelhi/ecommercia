package ecommercia.model.orders;

import javafx.beans.property.*;
import java.time.LocalDate;

public class DeliveryDetails {
    private final IntegerProperty id;
    private final IntegerProperty orderId;
    private final StringProperty address;
    private final ObjectProperty<LocalDate> deliveryDate;
    private final StringProperty status;

    public DeliveryDetails(int id, int orderId, String address, LocalDate deliveryDate, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.address = new SimpleStringProperty(address);
        this.deliveryDate = new SimpleObjectProperty<>(deliveryDate);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate.get();
    }

    public ObjectProperty<LocalDate> deliveryDateProperty() {
        return deliveryDate;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}
