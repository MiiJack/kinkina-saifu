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
    private static final String SAVE_QUERY = "INSERT INTO transaction (id, account_id, amount, transaction_date) VALUES (?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM transaction WHERE id = ?";

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

    public Transaction save(Transaction transaction) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, transaction.getId());
            statement.setInt(2, transaction.getAccount().getId());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getTransactionDate()));
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
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        Account account = accountCrudOperations.findById(resultSet.getInt("account_id"));
        return new Transaction(
                resultSet.getInt("id"),
                account,
                resultSet.getBigDecimal("amount"),
                resultSet.getTimestamp("transaction_date").toLocalDateTime()
        );
    }
}
