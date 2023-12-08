package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OtherCrudOperations {
    private Connection connection;
    public OtherCrudOperations() {
        this.connection = ConnectionDB.getConnection();
    }
    private static final String FIND_CURRENCY_BY_ID_QUERY = "SELECT * FROM \"currency\" WHERE id = ?";
    private static final String FIND_MOST_RECENT_BALANCE_BY_ACCOUNT_ID_QUERY = "SELECT * FROM \"balance\" WHERE account_id = ? " +
            "ORDER BY modification_date DESC LIMIT 1";
    private static final String FIND_ALL_BALANCE_BY_ACCOUNT_ID_QUERY = "SELECT * FROM balance WHERE account_id =?";
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
                    balance.setValue(resultSet.getDouble(ColumnLabel.BalanceTable.VALUE));
                    balance.setModificationDate(resultSet.getTimestamp(ColumnLabel.BalanceTable.MODIFICATION_DATE).toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public List<Balance> findAllByAccountId(int accountId) {
        List<Balance> balances = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_BALANCE_BY_ACCOUNT_ID_QUERY)) {
            statement.setInt(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    balances.add(mapToBalance(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balances;
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

    private Balance mapToBalance(ResultSet resultSet) throws SQLException {
        return new Balance(
            resultSet.getDouble(ColumnLabel.BalanceTable.VALUE),
            resultSet.getTimestamp(ColumnLabel.BalanceTable.MODIFICATION_DATE).toLocalDateTime()
        );
    }

    // public double getConversionRate(int sourceCurrencyId, int targetCurrencyId) {
    //     double conversionRate = 0.0;
    //     try (PreparedStatement statement = connection.prepareStatement("SELECT rate FROM CurrencyValue WHERE source_currency_id = ? AND target_currency_id = ?")) {
    //         statement.setInt(1, sourceCurrencyId);
    //         statement.setInt(2, targetCurrencyId);
    //         try (ResultSet resultSet = statement.executeQuery()) {
    //             if (resultSet.next()) {
    //                conversionRate = resultSet.getDouble("rate");
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return conversionRate;
    // }
}