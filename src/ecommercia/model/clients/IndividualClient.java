package ecommercia.model.clients;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.sql.Timestamp;
import java.time.LocalDate;

public class IndividualClient extends Client {
    private final ObjectProperty<LocalDate> dateOfBirth;

    public IndividualClient(int id, String name, String email, String phoneNumber, Timestamp createdAt, LocalDate dateOfBirth, String city, String region) {
        super(id, name, email, phoneNumber, createdAt, city, region);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    @Override
    public String getClientType() {
        return "individual";
    }
}
