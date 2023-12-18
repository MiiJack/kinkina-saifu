package mg.fita.kinkinasaifu;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;
import mg.fita.kinkinasaifu.services.AccountService;
import mg.fita.kinkinasaifu.services.BalanceService;
import mg.fita.kinkinasaifu.services.TransactionService;

public class Main {
  public static void main(String[] args) throws SQLException {
    ConnectionDB.getConnection();

    // Create repository objects
    AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
    TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    AccountService accountService = new AccountService();
    BalanceService balanceService = new BalanceService();
    TransactionService transactionService = new TransactionService();

    // Create mock data
    Currency currency6 = otherCrudOperations.findById(6);
    Currency currency5 = otherCrudOperations.findById(5);
    Balance balance1 = new Balance(3500.00, LocalDateTime.now());
    Balance balance2 = new Balance(200.00, LocalDateTime.now());
    Balance balance3 = new Balance(200.0, LocalDateTime.now());
    Transaction transaction1 =
        new Transaction(
            6,
            "Testing N1",
            1000.00,
            LocalDateTime.now(),
            "Debit",
            "Chell",
            "GladOS",
            Category.COMMUNICATION_PC);
    Account account1 =
        new Account(6, "Savings", balance1, Arrays.asList(transaction1), currency6, "Bank");
    Transaction transaction2 =
        new Transaction(
            7,
            "Testing N2",
            700.00,
            LocalDateTime.now(),
            "Credit",
            "null",
            "nullcepter",
            Category.FOOD_DRINKS);
    Account account2 =
        new Account(
            7, "Current", balance2, Arrays.asList(transaction2, transaction1), currency5, "Cash");

    System.out.println("TD1");
    // Create instances of your models

    Transaction transaction3 =
        new Transaction(
            8, "Test", 100.0, LocalDateTime.now(), "Debit", "Samonella", "Egg", Category.OTHERS);

    Account account =
        new Account(
            7,
            "Savings",
            balance3,
            Arrays.asList(transaction2, transaction3),
            otherCrudOperations.findById(1),
            "Bank");
    accountCrudOperations.save(account);

    //    System.out.println("Transfer Money Result: " + account);
    //    System.out.println(
    //        "1. Créer une fonction qui permet d’effectuer une transaction dans un compte");

    //    Account result = accountService.performTransaction(account, Arrays.asList(transaction3));
    //    System.out.println("Perform Transaction Result: " + result);
    System.out.println(
        "2. Créer une fonction qui permet d’obtenir le solde d’un compte à une date et heure"
            + " donnée");

    Balance balance = balanceService.getBalanceAtDateTime(account, LocalDateTime.now());
    System.out.println("Get Balance At DateTime Result: " + balance);
    System.out.println(
        "3. Créer une fonction qui permet d’obtenir l’historique du solde d’un compte dans une"
            + " intervalle de date et heure donnée");

    List<Balance> balances =
        balanceService.getBalanceHistory(
            account, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
    System.out.println("Get Balance History Result: " + balances);
    //        findMostRecentBalance
    //    System.out.println(
    //        "4. Créer une fonction qui permet de faire un transfert d’argent entre deux comptes");
    // Test transferMoney
    //    transactionService.transferMoney(account, account1, 100.0);
    //    System.out.println(
    //        "5. Calculer la moyenne pondérée, le minimum, le maximum ou la médian"
    //            + " de la valeur de l’euro durant cette date");
    //        // Test getBalanceSummary
    //    double summary =
    //        balanceService.getBalanceSummary(
    //            account.getId(),
    //            LocalDateTime.now(),
    //            BalanceService.BalanceSummaryType.WEIGHTED_AVERAGE);
    //    System.out.println("Get Balance Summary Result: " + summary);

    // Test save methods
    //accountCrudOperations.save(account1);
    //transactionCrudOperations.save(transaction1);
    //accountCrudOperations.save(account2);
    //transactionCrudOperations.save(transaction2);
    //balanceCrudOperations.saveBalance(balance1, account1.getId());
    //balanceCrudOperations.saveBalance(balance2, account2.getId());

    // Test findAll methods
    List<Account> allAccounts = accountCrudOperations.findAll();
    List<Transaction> allTransactions = transactionCrudOperations.findAll();

    // Print the results
    allAccounts.forEach(System.out::println);
    allTransactions.forEach(System.out::println);
  }
}
