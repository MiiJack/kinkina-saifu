package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OtherCrudOperations {
    private Connection connection;
    public OtherCrudOperations() {
        this.connection = ConnectionDB.getConnection();
    }
    private static final String FIND_CURRENCY_BY_ID_QUERY = "SELECT * FROM \"currency\" WHERE id = ?";
    private static final String TRANSFER_HISTORY_INTERVAL = "SELECT * FROM \"transfer_history\" " +
        "WHERE transfer_date_time BETWEEN ? AND ?";
    private static final String GET_MOST_RECENT_CURRENCY_VALUE = "SELECT value FROM \"currency_value\" WHERE source_currency_id = ? " +
        "AND target_currency_id = ? ORDER BY effective_date LIMIT 1";

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

    public List<TransferHistory> findAllByTransferDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        List<TransferHistory> transferHistories = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(TRANSFER_HISTORY_INTERVAL)) {
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TransferHistory transferHistory = new TransferHistory();
                    transferHistory.setId(resultSet.getInt(ColumnLabel.TransferHistoryTable.ID));
                    transferHistory.setDebtorTransferId(resultSet
                            .getInt(ColumnLabel.TransferHistoryTable.DEBTOR_TRANSACTION_ID));
                    transferHistory.setCreditorTransferId(resultSet
                            .getInt(ColumnLabel.TransferHistoryTable.CREDITOR_TRANSACTION_ID));
                    transferHistory.setTransferDateTime(resultSet
                            .getTimestamp(ColumnLabel.TransferHistoryTable.TRANSFER_DATE_TIME).toLocalDateTime());
                    transferHistories.add(transferHistory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transferHistories;
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