package mg.fita.kinkinasaifu.repositories;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

public class BalanceCrudOperations {
  private Connection connection;

  public BalanceCrudOperations() {
    this.connection = ConnectionDB.getConnection();
  }

  private static final String FIND_MOST_RECENT_BALANCE_BY_ACCOUNT_ID_QUERY =
      "SELECT * FROM \"balance\" " + "WHERE account_id = ? ORDER BY modification_date DESC LIMIT 1";
  private static final String FIND_ALL_BALANCE_BY_ACCOUNT_ID_QUERY =
      "SELECT * FROM \"balance\" " + "WHERE account_id =?";
  private static final String SAVE_BALANCE_QUERY =
      "INSERT INTO \"balance\" " + "(account_id, value, modification_date) VALUES (?, ?, ?)";

  public Balance findMostRecentBalance(int account_id) throws SQLException {
    Balance balance = null;
    try (PreparedStatement statement =
        connection.prepareStatement(FIND_MOST_RECENT_BALANCE_BY_ACCOUNT_ID_QUERY)) {
      statement.setInt(1, account_id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          balance = new Balance();
          balance.setValue(resultSet.getDouble(ColumnLabel.BalanceTable.VALUE));
          balance.setModificationDate(
              resultSet.getTimestamp(ColumnLabel.BalanceTable.MODIFICATION_DATE).toLocalDateTime());
        }
      }
    }
    return balance;
  }

  public List<Balance> findAllByAccountId(int accountId) throws SQLException {
    List<Balance> balances = new ArrayList<>();

    try (PreparedStatement statement =
        connection.prepareStatement(FIND_ALL_BALANCE_BY_ACCOUNT_ID_QUERY)) {
      statement.setInt(1, accountId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          balances.add(mapToBalance(resultSet));
        }
      }
    }

    return balances;
  }

  public void saveBalance(Balance balance, int account_id) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(SAVE_BALANCE_QUERY)) {
      statement.setInt(1, account_id);
      statement.setDouble(2, balance.getValue());
      statement.setTimestamp(3, Timestamp.valueOf(balance.getModificationDate()));
      statement.executeUpdate();
    }
  }

  private Balance mapToBalance(ResultSet resultSet) throws SQLException {
    return new Balance(
        resultSet.getDouble(ColumnLabel.BalanceTable.VALUE),
        resultSet.getTimestamp(ColumnLabel.BalanceTable.MODIFICATION_DATE).toLocalDateTime());
  }
}
