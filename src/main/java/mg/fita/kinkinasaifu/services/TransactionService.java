package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.Account;
import mg.fita.kinkinasaifu.model.Transaction;

import java.time.LocalDateTime;

public class TransactionService {
    public void transferMoney(Account senderAccount, Account receiverAccount, double amount) {
        if (senderAccount.getId() == receiverAccount.getId()) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        // Handle currency conversion if necessary
        if (!senderAccount.getCurrency().getCode().equals(receiverAccount.getCurrency().getCode())) {
            // Get the currency conversion rate for the sender's currency to the receiver's currency
            double conversionRate = currencyCrudOperations.getConversionRate(senderAccount.getCurrency().getId(), receiverAccount.getCurrency().getId());

            // Convert the amount to the receiver's currency
            amount *= conversionRate;
        }

        // Create the debit transaction for the sender account
        Transaction debitTransaction = new Transaction("Transfer to " + receiverAccount.getName(), amount, LocalDateTime.now(), "Debit");
        performTransaction(senderAccount, debitTransaction);

        // Create the credit transaction for the receiver account
        Transaction creditTransaction = new Transaction("Transfer from " + senderAccount.getName(), amount, LocalDateTime.now(), "Credit");
        performTransaction(receiverAccount, creditTransaction);

        // Save the transfer history to the database
        TransferHistory transferHistory = new TransferHistory(debitTransaction.getId(), creditTransaction.getId(), LocalDateTime.now());
        transferHistoryCrudOperations.save(transferHistory);
    }

}
