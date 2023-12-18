package mg.fita.kinkinasaifu.repositories;

import java.sql.*;
import java.util.*;
import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

public class TransactionCrudOperations {
  private final Connection connection;

  public TransactionCrudOperations() {
    this.connection = ConnectionDB.getConnection();
  }

  private static final String FIND_ALL_QUERY = "SELECT * FROM \"transaction\"";
  private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"transaction\" WHERE id = ?";
  private static final String SAVE_QUERY =
      "INSERT INTO \"transaction\" (label, amount, type, date_time, sender, receiver, category)"
          + " VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM \"transaction\" WHERE id = ?";
  private static final String FIND_ALL_BY_ACCOUNT_ID_QUERY =
      "SELECT * FROM \"transaction\" WHERE id =?";

  public List<Transaction> findAll() {
    List<Transaction> transactions = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        transactions.add(mapToTransaction(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transactions;
  }

  public Transaction findById(int id) {
    Transaction transaction = null;
    try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          transaction = mapToTransaction(resultSet);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transaction;
  }

  public List<Transaction> findAllByAccountId(int accountId) {
    List<Transaction> transactions = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_ACCOUNT_ID_QUERY)) {
      statement.setInt(1, accountId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          transactions.add(mapToTransaction(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return transactions;
  }

  public Transaction save(Transaction transaction) {
    try (PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
      statement.setString(1, transaction.getLabel());
      statement.setDouble(2, transaction.getAmount());
      statement.setObject(3, transaction.getTransactionType(), java.sql.Types.OTHER);
      statement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDateTime()));
      statement.setString(5, transaction.getSender());
      statement.setString(6, transaction.getReceiver());
      statement.setObject(7, transaction.getCategory(), java.sql.Types.OTHER);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error occurred while saving transactions", e);
    }
    return transaction;
  }

  public List<Transaction> saveAll(List<Transaction> toSave) {
    toSave.forEach(this::save);
    return toSave;
  }

  public void delete(int id) {
    try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Transaction mapToTransaction(ResultSet resultSet) throws SQLException {
    return new Transaction(
        resultSet.getInt(ColumnLabel.TransactionTable.ID),
        resultSet.getString(ColumnLabel.TransactionTable.LABEL),
        resultSet.getDouble(ColumnLabel.TransactionTable.AMOUNT),
        resultSet.getTimestamp(ColumnLabel.TransactionTable.DATE_TIME).toLocalDateTime(),
        resultSet.getString(ColumnLabel.TransactionTable.TYPE),
        resultSet.getString(ColumnLabel.TransactionTable.SENDER),
        resultSet.getString(ColumnLabel.TransactionTable.RECEIVER),
        Optional.ofNullable(resultSet.getString(ColumnLabel.TransactionTable.CATEGORY))
            .map(Category::valueOf)
            .orElse(Category.UNKNOWN));
  }
}
