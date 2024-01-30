package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReCrudMapper {
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
    TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
    private Account mapToAccount(ResultSet resultSet) throws SQLException {
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
    private Balance mapToBalance(ResultSet resultSet) throws SQLException {
        return new Balance(
                resultSet.getLong(ColumnLabel.BalanceTable.ID),
                resultSet.getDouble(ColumnLabel.BalanceTable.VALUE),
                resultSet.getTimestamp(ColumnLabel.BalanceTable.MODIFICATION_DATE).toLocalDateTime());
    }
    private Transaction mapToTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getLong(ColumnLabel.TransactionTable.ID),
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
