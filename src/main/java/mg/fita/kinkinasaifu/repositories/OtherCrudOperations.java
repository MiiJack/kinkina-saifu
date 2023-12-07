package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

import java.sql.*;

public class OtherCrudOperations {
    private Connection connection;
    public OtherCrudOperations() {
        this.connection = ConnectionDB.getConnection();
    }
    private static final String FIND_CURRENCY_BY_ID_QUERY = "SELECT * FROM \"currency\" WHERE id = ?";
    private static final String FIND_MOST_RECENT_BALANCE_BY_ACCOUNT_ID_QUERY = "SELECT * FROM \"balance\" WHERE account_id = ? " +
            "ORDER BY modification_date DESC LIMIT 1";

    private static final String SAVE_BALANCE_QUERY = "INSERT INTO \"balance\" " +
            "(account_id, value, modification_date) VALUES (?, ?, ?)";

    public Currency findById(int id) {
        Currency currency = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_CURRENCY_BY_ID_QUERY)) {
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

    public Balance findMostRecentBalance(int account_id) {
        Balance balance = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_MOST_RECENT_BALANCE_BY_ACCOUNT_ID_QUERY)) {
            statement.setInt(1, account_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    balance = new Balance();
                    balance.setValue(resultSet.getDouble("value"));
                    balance.setModificationDate(resultSet.getTimestamp("modification_date").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void saveBalance(Balance balance, int account_id) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_BALANCE_QUERY)) {
            statement.setInt(1, account_id);
            statement.setDouble(2, balance.getValue());
            statement.setTimestamp(3, Timestamp.valueOf(balance.getModificationDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}