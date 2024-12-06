package ecommercia.model.suppliers;

import javafx.beans.property.*;

public class Supplier {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phoneNumber;
    private final StringProperty city;
    private final StringProperty region;

    public Supplier(int id, String name, String email, String phoneNumber, String city, String region) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.city = new SimpleStringProperty(city);
        this.region = new SimpleStringProperty(region);
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

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getRegion() {
        return region.get();
    }

    public StringProperty regionProperty() {
        return region;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setRegion(String region) {
        this.region.set(region);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
