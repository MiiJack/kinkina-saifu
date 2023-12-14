CREATE FUNCTION SumEntriesAndExits(accountId INT, startDateTime DATETIME, endDateTime DATETIME)
RETURNS FLOAT AS    
BEGIN
   DECLARE @total DOUBLE;

   SELECT @total = SUM(amount)
   FROM transactions
   WHERE accountId = @accountId AND transactionDateTime BETWEEN @startDateTime AND @endDateTime;

   RETURN @total;
END;
