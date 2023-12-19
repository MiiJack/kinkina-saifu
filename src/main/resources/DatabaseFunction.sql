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

CREATE OR REPLACE FUNCTION calculate_category_sums(
  account_id_param INT,
  start_date TIMESTAMP,
  end_date TIMESTAMP
)
RETURNS TABLE(category VARCHAR, total DOUBLE PRECISION) AS $$
BEGIN
  RETURN QUERY (
      SELECT transaction.category, 
      SUM(
          CASE 
              WHEN transaction.category IS NULL THEN 0
              ELSE transaction.amount
          END
      ) AS total
      FROM transaction
      LEFT JOIN account_transaction ON transaction.id = account_transaction.transaction_id
      WHERE account_transaction.account_id = account_id_param
      AND transaction.date_time BETWEEN start_date AND end_date
      GROUP BY transaction.category
  );
END;
$$ LANGUAGE plpgsql;