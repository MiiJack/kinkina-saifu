CREATE FUNCTION SumCategories(
   account_id INT,
   start_date TIMESTAMP,
   end_date TIMESTAMP) 
RETURNS TABLE (category VARCHAR(255), total_amount DOUBLE PRECISION) AS $$
BEGIN
    RETURN QUERY
    SELECT
        category,
        COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS total_amount
    FROM (
        SELECT DISTINCT label AS category
        FROM "transaction"
    ) categories
    LEFT JOIN "transaction" t ON categories.category = t.label
    AND t.date_time BETWEEN start_date AND end_date
    AND EXISTS (
        SELECT 1
        FROM "account_transaction" at
        WHERE at.account_id = account_id
        AND at.transaction_id = t.id
    )
    GROUP BY category;
    RETURN;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_money_in_out(
  account_id_param INT,
  start_date TIMESTAMP,
  end_date TIMESTAMP
)
RETURNS DOUBLE PRECISION AS $$
BEGIN
  RETURN (
      SELECT SUM(
          CASE 
              WHEN type = 'Credit' THEN amount
              WHEN type = 'Debit' THEN -amount
          END
      )
      FROM transaction
      JOIN account_transaction ON transaction.id = account_transaction.transaction_id
      WHERE account_transaction.account_id = account_id_param
      AND transaction.date_time BETWEEN start_date AND end_date
  );
END;
$$ LANGUAGE plpgsql;