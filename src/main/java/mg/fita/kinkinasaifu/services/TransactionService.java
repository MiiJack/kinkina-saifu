package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();

//    public double getConversionRate(int sourceCurrencyId, int targetCurrencyId) {
//        double conversionRate = 0.0;
//        try (PreparedStatement statement = connection.prepareStatement(
//                "SELECT rate FROM CurrencyValue WHERE source_currency_id = ? AND target_currency_id = ?")) {
//            statement.setInt(1, sourceCurrencyId);
//            statement.setInt(2, targetCurrencyId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    conversionRate = resultSet.getDouble("rate");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return conversionRate;
//    }

//    public void transferMoney(Account senderAccount, Account receiverAccount, double amount) {
//        if (senderAccount.getId() == receiverAccount.getId()) {
//            throw new IllegalArgumentException("Cannot transfer money to the same account");
//        }
//
//        // Handle currency conversion if necessary
//        if (!senderAccount.getCurrency().getCode().equals(receiverAccount.getCurrency().getCode())) {
//            // Get the currency conversion rate for the sender's currency to the receiver's
//            // currency
//            double conversionRate = otherCrudOperations.getConversionRate(senderAccount.getCurrency().getId(),
//                    receiverAccount.getCurrency().getId());
//
//            // Convert the amount to the receiver's currency
//            amount *= conversionRate;
//        }
//
//        // Create the debit transaction for the sender account
//        Transaction debitTransaction = new Transaction(1, "Debit", amount, LocalDateTime.now(), "Debit", null, null);
//        performTransaction(senderAccount, debitTransaction);
//
//        // Create the credit transaction for the receiver account
//        Transaction creditTransaction = new Transaction(0, "Transfer from " + senderAccount.getName(), amount,
//                LocalDateTime.now(), "Credit", null, null);
//        performTransaction(receiverAccount, creditTransaction);
//
//        // Save the transfer history to the database
//        TransferHistory transferHistory = new TransferHistory(
//                debitTransaction.getId(),
//                creditTransaction.getId(),
//                senderAccount.getCurrency().getId(),
//                LocalDateTime.now());
//        transferHistoryCrudOperations.save(transferHistory);
//    }
}