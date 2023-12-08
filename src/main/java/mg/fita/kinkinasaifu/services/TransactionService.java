package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.Account;
import mg.fita.kinkinasaifu.model.Transaction;
import mg.fita.kinkinasaifu.model.TransferHistory;
import mg.fita.kinkinasaifu.repositories.OtherCrudOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private Connection connection;
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();

    public double getConversionRate(int sourceCurrencyId, int targetCurrencyId) {
        double conversionRate = 0.0;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT rate FROM CurrencyValue WHERE source_currency_id = ? AND target_currency_id = ?")) {
            statement.setInt(1, sourceCurrencyId);
            statement.setInt(2, targetCurrencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    conversionRate = resultSet.getDouble("rate");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversionRate;
    }

    public void transferMoney(Account senderAccount, Account receiverAccount, double amount) {
        if (senderAccount.getId() == receiverAccount.getId()) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        // Handle currency conversion if necessary
        if (!senderAccount.getCurrency().getCode().equals(receiverAccount.getCurrency().getCode())) {
            // Get the currency conversion rate for the sender's currency to the receiver's
            // currency
            double conversionRate = otherCrudOperations.getConversionRate(senderAccount.getCurrency().getId(),
                    receiverAccount.getCurrency().getId());

            // Convert the amount to the receiver's currency
            amount *= conversionRate;
        }

        // Create the debit transaction for the sender account
        Transaction debitTransaction = new Transaction(1, "Debit", amount, LocalDateTime.now(), "Debit", null, null);
        performTransaction(senderAccount, debitTransaction);

        // Create the credit transaction for the receiver account
        Transaction creditTransaction = new Transaction(0, "Transfer from " + senderAccount.getName(), amount,
                LocalDateTime.now(), "Credit", null, null);
        performTransaction(receiverAccount, creditTransaction);

        // Save the transfer history to the database
        TransferHistory transferHistory = new TransferHistory(
                debitTransaction.getId(),
                creditTransaction.getId(),
                senderAccount.getCurrency().getId(),
                LocalDateTime.now());
        transferHistoryCrudOperations.save(transferHistory);
    }

    public class TransferHistoryCrudOperations {
        private Connection connection;

        public TransferHistoryCrudOperations() {
            this.connection = ConnectionDB.getConnection();
        }

        public TransferHistory save(TransferHistory transferHistory) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO TransferHistory (debit_transaction_id, credit_transaction_id, transfer_date) VALUES (?, ?, ?)")) {
                statement.setInt(1, transferHistory.getDebitorCurrrencyId());
                statement.setInt(2, transferHistory.getCreditorCurrencyId());
                statement.setTimestamp(3, java.sql.Timestamp.valueOf(transferHistory.getTransferDate()));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return transferHistory;
        }

        public List<TransferHistory> findAllWithinRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            List<TransferHistory> transferHistories = new ArrayList<>();
            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM TransferHistory WHERE transfer_date >= ? AND transfer_date < ?")) {
                statement.setTimestamp(1, java.sql.Timestamp.valueOf(startDateTime));
                statement.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTime));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        TransferHistory transferHistory = new TransferHistory();
                        transferHistory.setId(resultSet.getInt("id"));
                        transferHistory.setDebitorCurrrencyId(resultSet.getInt("debitor_currency_id"));
                        transferHistory.setCreditorCurrencyId(resultSet.getInt("creditor_currency_id"));
                        transferHistory.setTransferDate(resultSet.getTimestamp("transfer_date").toLocalDateTime());
                        transferHistories.add(transferHistory);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return transferHistories;
        }
    }

}
