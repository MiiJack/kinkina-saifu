package mg.fita.kinkinasaifu.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

public class BalanceService {
  BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
  TransactionService transactionService = new TransactionService();
  TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
  TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();
  OtherCrudOperations otherCrudOperations = new OtherCrudOperations();

  public Balance getBalanceAtDateTime(Account account, LocalDateTime dateTime) throws SQLException {
    List<Balance> balances = balanceCrudOperations.findAllByAccountId(account.getId());
    for (int i = 0; i < balances.size(); i++) {
      if (balances.get(i).getModificationDate().isAfter(dateTime)) {
        return balances.get(i - 1);
      }
    }
    return balances.get(balances.size() - 1);
  }

  public List<Balance> getBalanceHistory(
      Account account, LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
    List<Balance> balances = balanceCrudOperations.findAllByAccountId(account.getId());

    return balances.stream()
        .filter(
            balance ->
                balance.getModificationDate().isAfter(startDateTime)
                    && balance.getModificationDate().isBefore(endDateTime))
        .collect(Collectors.toList());
  }

  private static Transaction latestTransaction;

  public double getBalanceSummary(int accountId, LocalDateTime date, BalanceSummaryType type)
      throws SQLException {
    double totalBalance = 0.0;
    double totalWeight = 0.0;
    Transaction latestTransaction = transactionService.findLatestTransaction(accountId, date);
    List<TransferHistory> transferHistoryList =
        transferHistoryCrudOperations.findAllWithinRange(
            date.with(LocalTime.MIN), date.with(LocalTime.MAX));
    if (latestTransaction != null) {
      latestTransaction = null;
    }
    BalanceService.latestTransaction = latestTransaction;
    for (TransferHistory transferHistory : transferHistoryList) {
      double amount = transferHistory.getAmount();
      if (transferHistory.getDebtorTransferId() == latestTransaction.getId()) {
        amount = -amount;
      }
      Transaction sourceTransaction =
          transactionService.findLatestTransaction(transferHistory.getDebtorTransferId(), date);
      Transaction targetTransaction =
          transactionService.findLatestTransaction(transferHistory.getCreditorTransferId(), date);
      Currency sourceCurrency = otherCrudOperations.findById(sourceTransaction.getId());
      Currency targetCurrency = otherCrudOperations.findById(targetTransaction.getId());
      double conversionRate =
          otherCrudOperations.getCurrencyValue(sourceCurrency.getId(), targetCurrency.getId());
      amount *= conversionRate;
      totalBalance += amount;
      totalWeight += 1.0;
    }

    switch (type) {
      case WEIGHTED_AVERAGE:
        return totalBalance / totalWeight;
      case MINIMUM:
        return findMinimumWithinTransferHistory(transferHistoryList);
      case MAXIMUM:
        return findMaximumWithinTransferHistory(transferHistoryList);
      case MEDIAN:
        List<Double> amounts = new ArrayList<>(); // Create a list to store all amounts
        for (TransferHistory transferHistory : transferHistoryList) {
          amounts.add(transferHistory.getAmount()); // Add each amount to the list
        }
        amounts.sort(Double::compareTo); // Sort the list of amounts in ascending order
        int medianIndex =
            (amounts.size() - 1) / 2; // Calculate the median index (middle of the sorted list)
        if (amounts.size() % 2 == 0) { // If even number of elements, return average of middle two
          return (amounts.get(medianIndex) + amounts.get(medianIndex + 1)) / 2.0;
        } else { // If odd number of elements, return value at median index
          return amounts.get(medianIndex);
        }
      default:
        throw new IllegalArgumentException("Invalid BalanceSummaryType: " + type);
    }
  }

  public enum BalanceSummaryType {
    WEIGHTED_AVERAGE,
    MINIMUM,
    MAXIMUM,
    MEDIAN
  }

  private double findMinimumWithinTransferHistory(List<TransferHistory> transferHistoryList) {
    double minimumAmount = Double.MAX_VALUE;
    for (TransferHistory transferHistory : transferHistoryList) {
      double amount = transferHistory.getAmount();
      if (transferHistory.getDebtorTransferId() == latestTransaction.getId()) {
        amount = -amount;
      }
      if (amount < minimumAmount) {
        minimumAmount = amount;
      }
    }
    return minimumAmount;
  }

  private double findMaximumWithinTransferHistory(List<TransferHistory> transferHistoryList) {
    double maximumAmount = Double.MIN_VALUE;
    for (TransferHistory transferHistory : transferHistoryList) {
      double amount = transferHistory.getAmount();
      if (transferHistory.getDebtorTransferId() == latestTransaction.getId()) {
        amount = -amount;
      }
      if (amount > maximumAmount) {
        maximumAmount = amount;
      }
    }
    return maximumAmount;
  }
}
