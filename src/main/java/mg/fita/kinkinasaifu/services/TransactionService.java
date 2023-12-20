package mg.fita.kinkinasaifu.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

public class TransactionService {
  OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
  BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
  AccountCrudOperations accountCrudOperations = new AccountCrudOperations();

  TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

  TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();
  AccountService accountService = new AccountService();

  public Transaction findLatestTransaction(int accountId, LocalDateTime date) {
    List<Transaction> transactions = transactionCrudOperations.findAllByAccountId(accountId);
    if (date == null) {
      return Collections.max(transactions, Comparator.comparingInt(Transaction::getId));
    }
    return transactions.stream()
        .filter(transaction -> transaction.getDateTime().toLocalDate().equals(date.toLocalDate()))
        .max(Comparator.comparing(Transaction::getDateTime))
        .orElse(null);
  }

  public Transaction findLatestTransaction(int accountId) {
    return findLatestTransaction(accountId, null);
  }

  public void transferMoney(Account senderAccount, Account receiverAccount, double amount)
      throws SQLException {
    if (senderAccount.getId() == receiverAccount.getId()) {
      throw new IllegalArgumentException("Cannot transfer money to the same account");
    }
    if (senderAccount.getBalance().getValue() < amount) {
      throw new IllegalArgumentException("Insufficient funds for sender account");
    }
    double convertedAmount = amount;
    if (!senderAccount.getCurrency().getCode().equals(receiverAccount.getCurrency().getCode())) {
      double conversionRate =
          otherCrudOperations.getCurrencyValue(
              senderAccount.getCurrency().getId(), receiverAccount.getCurrency().getId());

      conversionRate =
          (Double.isNaN(conversionRate) || Double.isInfinite(conversionRate))
              ? 0.0
              : conversionRate;
      convertedAmount = amount * conversionRate;
    }

    Transaction latestSenderTransaction = findLatestTransaction(senderAccount.getId());
    Transaction latestReceiverTransaction = findLatestTransaction(receiverAccount.getId());

    int nextSenderTransactionId =
        latestSenderTransaction == null ? 1 : latestSenderTransaction.getId() + 1;
    int nextReceiverTransactionId =
        latestReceiverTransaction == null ? 1 : latestReceiverTransaction.getId() + 1;

    Balance senderBalance = senderAccount.getBalance();
    senderBalance.setValue(senderBalance.getValue() - convertedAmount);
    balanceCrudOperations.saveBalance(senderBalance, senderAccount.getId());

    TransferHistory transferHistory = new TransferHistory();
    transferHistory.setDebtorTransferId(senderAccount.getTransactions().size() + 1);
    transferHistory.setCreditorTransferId(receiverAccount.getTransactions().size() + 1);
    transferHistory.setAmount(convertedAmount);
    transferHistory.setTransferDateTime(LocalDateTime.now());
    transferHistoryCrudOperations.save(transferHistory);

    Transaction senderTransaction =
        new Transaction(
            nextSenderTransactionId,
            "Transfer to "
                + receiverAccount.getName()
                + " ("
                + receiverAccount.getCurrency().getCode()
                + ")",
            amount,
            LocalDateTime.now(),
            "Debit",
            senderAccount.getName(),
            receiverAccount.getName(),
            receiverAccount.getCategory());

    Transaction receiverTransaction =
        new Transaction(
            nextReceiverTransactionId,
            "Transfer Received from: "
                + senderAccount.getName()
                + " ("
                + senderAccount.getCurrency().getCode()
                + ")",
            convertedAmount,
            LocalDateTime.now(),
            "Credit",
            senderAccount.getName(),
            receiverAccount.getName(),
            receiverAccount.getCategory());
    transactionCrudOperations.save(receiverTransaction);
    transactionCrudOperations.save(senderTransaction);

    receiverAccount.getTransactions().add(receiverTransaction);

    senderAccount.getBalance().setValue(senderBalance.getValue());
    accountCrudOperations.save(senderAccount);
  }
}
