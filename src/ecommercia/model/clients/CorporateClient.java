package ecommercia.model.clients;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

public class CorporateClient extends Client {
    private final StringProperty companyName;
    private final StringProperty taxId;

    public CorporateClient(int id, String name, String email, String phoneNumber, Timestamp createdAt, String companyName, String taxId, String city, String region) {
        super(id, name, email, phoneNumber, createdAt, city, region);
        this.companyName = new SimpleStringProperty(companyName);
        this.taxId = new SimpleStringProperty(taxId);
    }


    // Getters and Setters for companyName
    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    // Getters and Setters for taxId
    public String getTaxId() {
        return taxId.get();
    }

    public void setTaxId(String taxId) {
        this.taxId.set(taxId);
    }

    public StringProperty taxIdProperty() {
        return taxId;
    }

    @Override
    public String getClientType() {
        return "corporate";
    }
}
