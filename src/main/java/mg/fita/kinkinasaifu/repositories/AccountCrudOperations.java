package mg.fita.kinkinasaifu.repositories;

import java.sql.*;
import java.util.*;
import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.model.ColumnLabel;
import mg.fita.kinkinasaifu.model.Currency;

public class AccountCrudOperations {
  private final Connection connection;

  public AccountCrudOperations() {
    this.connection = ConnectionDB.getConnection();
  }

  private static final String FIND_ALL_QUERY = "SELECT * FROM \"account\"";
  private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"account\" WHERE id = ?";
  private static final String SAVE_QUERY =
      "INSERT INTO \"account\" (name, type, currency_id) VALUES (?, ?, ?)";

  /*private static final String DELETE_QUERY = "DELETE FROM \"account\" WHERE id = ?";*/

  public List<Account> findAll() throws SQLException {
    List<Account> accounts = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        accounts.add(mapToAccount(resultSet));
      }
    }
    return accounts;
  }

  public Account findById(long id) throws SQLException {
    Account account = null;
    try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          account = mapToAccount(resultSet);
        }
      }
    }
    return account;
  }

  public Account save(Account account) {
    try (PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
      statement.setObject(1, account.getName(), java.sql.Types.OTHER);
      statement.setObject(2, account.getType(), java.sql.Types.OTHER);
      statement.setLong(3, account.getCurrency().getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error occurred while saving account", e);
    }
    return account;
  }

  public List<Account> saveAll(List<Account> toSave) {
    toSave.forEach(this::save);
    return toSave;
  }

  /*    public void delete(long id) throws SQLException{
      try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
          statement.setLong(1, id);
          statement.executeUpdate();
      }
  } */

  private Account mapToAccount(ResultSet resultSet) throws SQLException {
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
    TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

    Currency currency =
        otherCrudOperations.findById(resultSet.getLong(ColumnLabel.AccountTable.CURRENCY_ID));
    Balance balance =
        balanceCrudOperations.findMostRecentBalance(resultSet.getLong(ColumnLabel.AccountTable.ID));
    List<Transaction> transactions =
        transactionCrudOperations.findAllByAccountId(
            resultSet.getLong(ColumnLabel.TransactionTable.ID));

    return new Account(
        resultSet.getLong(ColumnLabel.AccountTable.ID),
        resultSet.getString(ColumnLabel.AccountTable.NAME),
        balance,
        transactions,
        currency,
        resultSet.getString(ColumnLabel.AccountTable.TYPE));
  }
}
