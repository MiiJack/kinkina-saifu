package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

import java.time.LocalDateTime;

public class AccountService {
    AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    public Account performTransaction(Account account, Transaction transaction) {
        account.getTransactions().add(transaction);
        Balance balance = account.getBalance();
        if (transaction.getTransactionType().equals("Debit")) {
            balance.setValue(balance.getValue() - transaction.getAmount());
        } else if (transaction.getTransactionType().equals("Credit")) {
            balance.setValue(balance.getValue() + transaction.getAmount());
        }

        balance.setModificationDate(LocalDateTime.now());

        // Save the updated account to the database
        accountCrudOperations.save(account);
        return accountCrudOperations.findById(account.getId());
    }

}
