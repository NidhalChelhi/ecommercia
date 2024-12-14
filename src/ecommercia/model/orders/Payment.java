package ecommercia.model.orders;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Payment {
    private final IntegerProperty id;
    private final IntegerProperty orderId;
    private final ObjectProperty<LocalDate> paymentDate;
    private final DoubleProperty amount;
    private final StringProperty paymentMethod;

    public Payment(int id, int orderId, LocalDate paymentDate, double amount, String paymentMethod) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
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

    public LocalDate getPaymentDate() {
        return paymentDate.get();
    }

    public ObjectProperty<LocalDate> paymentDateProperty() {
        return paymentDate;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }
}
