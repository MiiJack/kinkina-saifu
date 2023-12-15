package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;

import java.sql.*;
import java.util.*;

public class TransactionCrudOperations{
    private Connection connection;
    public TransactionCrudOperations() {
        this.connection = ConnectionDB.getConnection();
    }
    private static final String FIND_ALL_QUERY = "SELECT * FROM transaction";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM transaction WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO transaction (id, label, amount, type, date_time, sender, receiver)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM transaction WHERE id = ?";
    private static final String FIND_ALL_BY_ACCOUNT_ID_QUERY = "SELECT * FROM transaction WHERE id =?";

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
            statement.setInt(1, transaction.getId());
            statement.setString(2, transaction.getLabel());
            statement.setDouble(3, transaction.getAmount());
            statement.setObject(4, transaction.getTransactionType(), java.sql.Types.OTHER);
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(transaction.getDateTime()));
            statement.setString(6, transaction.getSender());
            statement.setString(7, transaction.getReceiver());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            (Category) resultSet.getObject(ColumnLabel.TransactionTable.CATEGORY)
        );
    }

}