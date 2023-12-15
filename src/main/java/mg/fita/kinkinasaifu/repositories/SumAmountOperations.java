package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.connection.ConnectionDB;
import mg.fita.kinkinasaifu.model.SumAmount;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SumAmountOperations {
    private Connection connection;

    public SumAmountOperations() {
        this.connection = ConnectionDB.getConnection();
    }

    public List<SumAmount> calculateSumAmounts(Timestamp startDate, Timestamp endDate, int accountId) {
        List<SumAmount> sumAmounts = new ArrayList<>();

        final String SUM_AMOUNT_QUERY = "SELECT category, COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS total_amount " +
                "FROM (SELECT DISTINCT label AS category FROM transaction) categories " +
                "LEFT JOIN transaction t ON categories.category = t.label " +
                "AND t.date_time BETWEEN ? AND ? " +
                "AND EXISTS (SELECT 1 FROM account_transaction at WHERE at.account_id = ? AND at.transaction_id = t.id) " +
                "GROUP BY category";

        try (PreparedStatement statement = connection.prepareStatement(SUM_AMOUNT_QUERY)) {
            statement.setTimestamp(1, startDate);
            statement.setTimestamp(2, endDate);
            statement.setInt(3, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String category = resultSet.getString("category");
                    double totalAmount = resultSet.getDouble("total_amount");
                    SumAmount sumAmount = new SumAmount(category, totalAmount);
                    sumAmounts.add(sumAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sumAmounts;
    }

        public List<SumAmount> calculateMappedSumAmounts(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<SumAmount> sumAmounts = new ArrayList<>();

        String TRANSACTIONS_BY_DATE_QUERY = "SELECT label, amount, date_time, type FROM transaction " +
                "JOIN account_transaction ON transaction.id = account_transaction.transaction_id " +
                "WHERE account_transaction.account_id = ? AND date_time BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(TRANSACTIONS_BY_DATE_QUERY)) {
            statement.setInt(1, accountId);
            statement.setTimestamp(2, Timestamp.valueOf(startDate));
            statement.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String category = resultSet.getString("label");
                    double amount = resultSet.getDouble("amount");
                    String type = resultSet.getString("type");

                    if ("Credit".equals(type)) {
                        amount = -amount; // Convert Credit to negative amount
                    }

                    SumAmount sumAmount = new SumAmount(category, amount);
                    sumAmounts.add(sumAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Map data and calculate total amounts for each category
        List<SumAmount> mappedSumAmounts = new ArrayList<>();
        for (SumAmount sumAmount : sumAmounts) {
            String category = sumAmount.getCategory();
            double totalAmount = sumAmounts.stream()
                    .filter(sa -> sa.getCategory().equals(category))
                    .mapToDouble(SumAmount::getTotalAmount)
                    .sum();

            SumAmount mappedSumAmount = new SumAmount(category, totalAmount);
            if (!mappedSumAmounts.contains(mappedSumAmount)) {
                mappedSumAmounts.add(mappedSumAmount);
            }
        }

        return mappedSumAmounts;
    }
}
