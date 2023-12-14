CREATE FUNCTION SumEntriesAndExits(account_id INT, start_date TIMESTAMP, end_date TIMESTAMP) 
RETURNS DOUBLE PRECISION AS $$
DECLARE
    total_amount DOUBLE PRECISION;
BEGIN
    SELECT COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0)
    INTO total_amount
    FROM "transaction" t
    WHERE t.date_time BETWEEN start_date AND end_date
    AND EXISTS (
        SELECT 1
        FROM "account_transaction" at
        WHERE at.account_id = account_id
        AND at.transaction_id = t.id
    );
    RETURN total_amount;
END;
$$ LANGUAGE plpgsql;

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
