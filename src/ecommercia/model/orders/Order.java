package ecommercia.model.orders;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Order {
    private final IntegerProperty id;
    private final IntegerProperty clientId;
    private final ObjectProperty<LocalDate> orderDate;
    private final ObjectProperty<OrderStatus> status;
    private final ObjectProperty<PaymentMethod> paymentMethod;
    private final DoubleProperty totalAmount;

    public Order(int id, int clientId, LocalDate orderDate, OrderStatus status, PaymentMethod paymentMethod, double totalAmount) {
        this.id = new SimpleIntegerProperty(id);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.orderDate = new SimpleObjectProperty<>(orderDate);
        this.status = new SimpleObjectProperty<>(status);
        this.paymentMethod = new SimpleObjectProperty<>(paymentMethod);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
    }

    // Property methods for status
    public ObjectProperty<OrderStatus> statusProperty() {
        return status;
    }

    public OrderStatus getStatus() {
        return status.get();
    }

    public void setStatus(OrderStatus status) {
        this.status.set(status);
    }

    // Property methods for payment method
    public ObjectProperty<PaymentMethod> paymentMethodProperty() {
        return paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod.get();
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    // Other property methods
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public LocalDate getOrderDate() {
        return orderDate.get();
    }

    public ObjectProperty<LocalDate> orderDateProperty() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }
}
