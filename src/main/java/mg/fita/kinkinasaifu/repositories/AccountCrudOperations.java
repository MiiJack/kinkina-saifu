package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.Configuration.DatabaseConfiguration;
import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.model.Currency;

import java.sql.*;
import java.util.*;

public class AccountCrudOperations{
    private static final String FIND_ALL_QUERY = "SELECT * FROM \"account\"";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"account\" WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO \"account\" (id, principal_currency_id, name) VALUES (?, ?, ?)";
    /*private static final String DELETE_QUERY = "DELETE FROM \"account\" WHERE id = ?";*/

    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(mapToAccount(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account findById(int id) {
        Account account = null;
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    account = mapToAccount(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account save(Account account) {
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, account.getId());
            statement.setString(2, account.getPrincipalCurrency().toString());
            statement.setString(3, account.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public List<Account> saveAll(List<Account> toSave) {
        toSave.forEach(this::save);
        return toSave;
    }
/*    public void delete(int id) {
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } */

    private Account mapToAccount(ResultSet resultSet) throws SQLException {
        CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
        Currency principalCurrency = currencyCrudOperations.findById(resultSet.getInt("principal_currency_id"));
        return new Account(
                resultSet.getInt("id"),
                principalCurrency,
                resultSet.getString("name")
        );
    }
}