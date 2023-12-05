package mg.fita.kinkinasaifu;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.repositories.*;
import mg.fita.kinkinasaifu.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectionDB.getConnection();
        // Create mock data
        Currency currency = new Currency(1, "USD", "Dollar");
        Account account = new Account(7, currency, "Test Account");
        Transaction transaction = new Transaction(1, account, BigDecimal.valueOf(1000), LocalDateTime.now());

        // Create repository objects
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

        // Test save methods
        accountCrudOperations.save(account);
        transactionCrudOperations.save(transaction);

        // Test findAll methods
        List<Account> allAccounts = accountCrudOperations.findAll();
        List<Transaction> allTransactions = transactionCrudOperations.findAll();

        // Print the results
        allAccounts.forEach(System.out::println);
        allTransactions.forEach(System.out::println);
    }
}