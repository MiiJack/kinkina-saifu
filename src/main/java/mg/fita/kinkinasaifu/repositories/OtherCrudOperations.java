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
    private static final String GET_MOST_RECENT_CURRENCY_VALUE = "SELECT value FROM \"currency_value\" WHERE source_currency_id = ? " +
        "AND target_currency_id = ? ORDER BY effective_date LIMIT 1";

    public Currency findById(int id) {
        Currency currency = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_CURRENCY_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = new Currency(
                            resultSet.getInt(ColumnLabel.CurrencyTable.ID),
                            resultSet.getString(ColumnLabel.CurrencyTable.NAME),
                            resultSet.getString(ColumnLabel.CurrencyTable.CODE)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

     public double getCurrencyValue (int sourceCurrencyId, int targetCurrencyId) {
         double currencyValue = 0.0;
         try (PreparedStatement statement = connection.prepareStatement(GET_MOST_RECENT_CURRENCY_VALUE)) {
             statement.setInt(1, sourceCurrencyId);
             statement.setInt(2, targetCurrencyId);
             try (ResultSet resultSet = statement.executeQuery()) {
                 if (resultSet.next()) {
                     currencyValue = resultSet.getDouble(ColumnLabel.CurrencyValueTable.VALUE);
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return currencyValue;
     }
}