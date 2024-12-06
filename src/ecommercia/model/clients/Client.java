package ecommercia.model.clients;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Timestamp;

public abstract class Client {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phoneNumber;

    private final StringProperty city;
    private final StringProperty region;
    private Timestamp createdAt;

    public Client(int id, String name, String email, String phoneNumber, Timestamp createdAt, String city, String region) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.createdAt = createdAt;
        this.city = new SimpleStringProperty(city);
        this.region = new SimpleStringProperty(region);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getRegion() {
        return region.get();
    }

    public void setRegion(String region) {
        this.region.set(region);
    }

    public StringProperty regionProperty() {
        return region;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public abstract String getClientType();
}
