package mg.fita.kinkinasaifu.repositories;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.ColumnLabel;
import mg.fita.kinkinasaifu.model.TransferHistory;

public class TransferHistoryCrudOperations {
  private final Connection connection;

  public TransferHistoryCrudOperations() {
    this.connection = ConnectionDB.getConnection();
  }

  private static final String SAVE_TRANSFER_HISTORY =
      "INSERT INTO \"transfer_history\"(debtor_transaction_id, creditor_transaction_id , amount,"
          + " transfer_date_time) VALUES (?, ?, ?, ?)";
  private static final String FIND_ALL_TRANSFER_HISTORY_WITHIN_RANGE =
      "SELECT * FROM \"transfer_history\" "
          + "WHERE transfer_date_time >= ? AND transfer_date_time < ?";

  public TransferHistory save(TransferHistory transferHistory) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(SAVE_TRANSFER_HISTORY)) {
      statement.setInt(1, transferHistory.getDebtorTransferId());
      statement.setInt(2, transferHistory.getCreditorTransferId());
      statement.setDouble(3, transferHistory.getAmount());
      statement.setTimestamp(4, java.sql.Timestamp.valueOf(transferHistory.getTransferDateTime()));
      statement.executeUpdate();
    }
    return transferHistory;
  }

  public List<TransferHistory> findAllWithinRange(
      LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
    List<TransferHistory> transferHistories = new ArrayList<>();
    try (PreparedStatement statement =
        connection.prepareStatement(FIND_ALL_TRANSFER_HISTORY_WITHIN_RANGE)) {
      statement.setTimestamp(1, java.sql.Timestamp.valueOf(startDateTime));
      statement.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTime));
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          TransferHistory transferHistory = new TransferHistory();
          transferHistory.setId(resultSet.getInt(ColumnLabel.TransferHistoryTable.ID));
          transferHistory.setDebtorTransferId(
              resultSet.getInt(ColumnLabel.TransferHistoryTable.DEBTOR_TRANSACTION_ID));
          transferHistory.setCreditorTransferId(
              resultSet.getInt(ColumnLabel.TransferHistoryTable.CREDITOR_TRANSACTION_ID));
          transferHistory.setTransferDateTime(
              resultSet
                  .getTimestamp(ColumnLabel.TransferHistoryTable.TRANSFER_DATE_TIME)
                  .toLocalDateTime());
          transferHistories.add(transferHistory);
        }
      }
    }
    return transferHistories;
  }
}
