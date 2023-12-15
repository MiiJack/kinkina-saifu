package mg.fita.kinkinasaifu;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.repositories.*;
import mg.fita.kinkinasaifu.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectionDB.getConnection();

        // Create repository objects
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        OtherCrudOperations otherCrudOperations = new OtherCrudOperations();

        // Create mock data
        Currency currency6 = otherCrudOperations.findById(6);
        Currency currency5 = otherCrudOperations.findById(5);
        Balance balance1 = new Balance(3500.00, LocalDateTime.now());
        Balance balance2 = new Balance(200.00, LocalDateTime.now());
        Transaction transaction1 = new Transaction(6, "Testing N1", 1000.00, LocalDateTime.now(),
                "Debit", null, null, Category.COMMUNICATION_PC);
        Account account1 = new Account(6, "Savings", balance1, List.of(transaction1), currency6, "Bank");
        Transaction transaction2 = new Transaction(7, "Testing N2", 700.00, LocalDateTime.now(),
                "Credit", null, null, Category.FOOD_DRINKS);
        Account account2 = new Account(7, "Current", balance2, List.of(transaction2,transaction1), currency5, "Cash");

        // Test save methods
        accountCrudOperations.save(account1);
        transactionCrudOperations.save(transaction1);
        accountCrudOperations.save(account2);
        transactionCrudOperations.save(transaction2);
        balanceCrudOperations.saveBalance(balance1,account1.getId());
        balanceCrudOperations.saveBalance(balance2,account2.getId());

        // Test findAll methods
        List<Account> allAccounts = accountCrudOperations.findAll();
        List<Transaction> allTransactions = transactionCrudOperations.findAll();

        // Print the results
        allAccounts.forEach(System.out::println);
        allTransactions.forEach(System.out::println);
    }
}