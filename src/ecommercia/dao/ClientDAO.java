package ecommercia.dao;

import ecommercia.model.clients.Client;
import ecommercia.model.clients.CorporateClient;
import ecommercia.model.clients.IndividualClient;
import ecommercia.utils.DatabaseUtility;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    /**
     * Adds a new client (Individual or Corporate) to the database.
     *
     * @param client The client object to be added.
     */
    public void add(Client client) {
        String query = "INSERT INTO clients (name, email, phone_number, type, date_of_birth, company_name, tax_id, city, region) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhoneNumber());
            statement.setString(4, client.getClientType());
            statement.setString(8, client.getCity());
            statement.setString(9, client.getRegion());

            if (client instanceof IndividualClient) {
                IndividualClient individualClient = (IndividualClient) client;
                statement.setDate(5, Date.valueOf(individualClient.getDateOfBirth()));
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
            } else if (client instanceof CorporateClient) {
                CorporateClient corporateClient = (CorporateClient) client;
                statement.setNull(5, Types.DATE);
                statement.setString(6, corporateClient.getCompanyName());
                statement.setString(7, corporateClient.getTaxId());
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client The client object with updated data.
     */
    public void update(Client client) {
        String query = "UPDATE clients SET name = ?, email = ?, phone_number = ?, date_of_birth = ?, " +
                "company_name = ?, tax_id = ?, city = ?, region = ? WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhoneNumber());
            statement.setString(7, client.getCity());
            statement.setString(8, client.getRegion());

            if (client instanceof IndividualClient) {
                IndividualClient individualClient = (IndividualClient) client;
                statement.setDate(4, Date.valueOf(individualClient.getDateOfBirth()));
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.VARCHAR);
            } else if (client instanceof CorporateClient) {
                CorporateClient corporateClient = (CorporateClient) client;
                statement.setNull(4, Types.DATE);
                statement.setString(5, corporateClient.getCompanyName());
                statement.setString(6, corporateClient.getTaxId());
            }

            statement.setInt(9, client.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a client from the database by ID.
     *
     * @param clientId The ID of the client to be deleted.
     */
    public void delete(int clientId) {
        String query = "DELETE FROM clients WHERE id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, clientId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches all clients from the database.
     *
     * @return A list of clients (Individual and Corporate).
     */
    public List<Client> findAll() {
        String query = "SELECT * FROM clients";
        List<Client> clients = new ArrayList<>();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                if ("individual".equalsIgnoreCase(type)) {
                    String rawDob = resultSet.getString("date_of_birth"); // Retrieve as string
                    LocalDate dob = null;

                    try {
                        // Attempt to parse if stored as yyyy-MM-dd
                        if (rawDob != null) {
                            dob = LocalDate.parse(rawDob); // ISO format
                        }
                    } catch (Exception e) {
                        try {
                            // Attempt to parse if stored as epoch time (milliseconds)
                            long epochMillis = Long.parseLong(rawDob);
                            dob = Instant.ofEpochMilli(epochMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                        } catch (NumberFormatException nfe) {
                            System.err.println("Unrecognized date format: " + rawDob);
                        }
                    }

                    clients.add(new IndividualClient(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone_number"),
                            resultSet.getTimestamp("created_at"),
                            dob,
                            resultSet.getString("city"),
                            resultSet.getString("region")
                    ));
                } else if ("corporate".equalsIgnoreCase(type)) {
                    clients.add(new CorporateClient(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone_number"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getString("company_name"),
                            resultSet.getString("tax_id"),
                            resultSet.getString("city"),
                            resultSet.getString("region")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    /**
     * Fetches all individual clients from the database.
     *
     * @return A list of individual clients.
     */
    public List<IndividualClient> findAllIndividualClients() {
        List<IndividualClient> individualClients = new ArrayList<>();
        for (Client client : findAll()) {
            if (client instanceof IndividualClient) {
                individualClients.add((IndividualClient) client);
            }
        }
        return individualClients;
    }

    /**
     * Fetches all corporate clients from the database.
     *
     * @return A list of corporate clients.
     */
    public List<CorporateClient> findAllCorporateClients() {
        List<CorporateClient> corporateClients = new ArrayList<>();
        for (Client client : findAll()) {
            if (client instanceof CorporateClient) {
                corporateClients.add((CorporateClient) client);
            }
        }
        return corporateClients;
    }
}
