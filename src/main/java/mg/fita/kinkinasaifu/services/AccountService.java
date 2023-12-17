package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccountService {
    AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
    public Account performTransaction(Account account, Transaction transaction) throws SQLException {
        account.getTransactions().add(transaction);
        Balance balance = account.getBalance();
        if (!account.getType().equals("Bank") && account.getBalance().getValue().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds for transaction");
        }
        if (transaction.getTransactionType().equals("Debit")) {
            balance.setValue(balance.getValue() - transaction.getAmount());
        } else if (transaction.getTransactionType().equals("Credit")) {
            balance.setValue(balance.getValue() + transaction.getAmount());
        }

        balance.setModificationDate(LocalDateTime.now());

        accountCrudOperations.save(account);
        transactionCrudOperations.save(transaction);
        return accountCrudOperations.findById(account.getId());
    }

}
