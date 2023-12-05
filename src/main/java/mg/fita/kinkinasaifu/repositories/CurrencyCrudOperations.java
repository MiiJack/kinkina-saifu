package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.Currency;

import java.sql.*;

public class CurrencyCrudOperations {
    private Connection connection;
    public CurrencyCrudOperations() {
        this.connection = ConnectionDB.getConnection();
    }
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"currency\" WHERE id = ?";

    public Currency findById(int id) {
        Currency currency = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = new Currency(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("code")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }
}
