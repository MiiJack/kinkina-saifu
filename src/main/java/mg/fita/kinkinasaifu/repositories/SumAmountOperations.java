package mg.fita.kinkinasaifu.repositories;

import mg.fita.kinkinasaifu.model.SumAmount;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class SumAmountOperations {
    
    public List<SumAmount> calculateSumAmounts(
            int accountId,
            Timestamp startDate,
            Timestamp endDate
    ) {
        List<SumAmount> SumAmounts = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://your-database-url", "your-username", "your-password")) {
            String sql = "SELECT category, COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS total_amount " +
                    "FROM (SELECT DISTINCT label AS category FROM transaction) categories " +
                    "LEFT JOIN transaction t ON categories.category = t.label " +
                    "AND t.date_time BETWEEN ? AND ? " +
                    "AND EXISTS (SELECT 1 FROM account_transaction at WHERE at.account_id = ? AND at.transaction_id = t.id) " +
                    "GROUP BY category";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setTimestamp(1, startDate);
                statement.setTimestamp(2, endDate);
                statement.setInt(3, accountId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String category = resultSet.getString("category");
                        double totalAmount = resultSet.getDouble("total_amount");
                        SumAmount SumAmount = new SumAmount(category, totalAmount);
                        SumAmounts.add(SumAmount);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return SumAmounts;
    }

    public static void main(String[] args) {
        SumAmountOperations calculator = new SumAmountOperations();
        List<SumAmount> result = calculator.calculateSumAmounts(1, Timestamp.valueOf("2023-12-01 00:00:00"), Timestamp.valueOf("2023-12-02 23:59:59"));

        // Print or use the result as needed
        for (SumAmount SumAmount : result) {
            System.out.println(SumAmount.getCategory() + " | " + SumAmount.getTotalAmount());
        }
    }
}
