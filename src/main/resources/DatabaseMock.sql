-- Mock data for currencies
INSERT INTO "currency" (name, code) VALUES
    ('United States Dollar', 'USD'),
    ('Malagasy Ariary', 'MGA'),
    ('Euro', 'EUR'),
    ('British Pound', 'GBP'),
    ('Japanese Yen', 'JPY'),
    ('Swiss Franc', 'CHF');

-- Mock data for accounts
INSERT INTO "account" (name, type, currency_id) VALUES
    ('Savings', 'Bank', 1),
    ('Current', 'Cash', 2),
    ('Savings', 'Mobile Money', 3),
    ('Current', 'Cash', 4),
    ('Savings', 'Bank', 5);

-- Mock data for balances
INSERT INTO "balance" (account_id, modification_date, value) VALUES
    (1, '2023-11-01 20:37:02', 950.00),
    (2, '2023-03-01 12:21:05', 2000.00),
    (3, '2023-03-01 03:04:01', 3000.00),
    (4, '2023-12-01 06:30:30', 4000.00),
    (2, '2023-03-02 12:21:07', 1500.00),
    (3, '2023-05-01 13:45:40', 2900.00),
    (4, '2023-12-02 06:30:30', 3500.00),
    (5, '2023-12-01 18:20:15', 5000.00);

-- Mock data for transactions
INSERT INTO "transaction" (label, amount, date_time, type, sender, receiver) VALUES
    ('Expense A', 50.00,  '2023-11-01 20:37:02', 'Debit', 'Andy', 'Vendor A'),
    ('Salary Deposit', 2000.00, '2023-03-01 12:21:05', 'Credit', 'Company B', 'Don Smokilla'),
    ('Expense B', 100.00, '2023-03-01 03:04:01', 'Debit', 'Charlie Brown', 'Vendor B'),
    ('Transfer C', 500.00, '2023-12-01 06:30:30', 'Debit','Deil Delta', 'Everbridge Vitas'),
    ('Expense D', 150.00,  '2023-12-01 18:20:15', 'Debit','Everbridge Vitas', 'Vendor C');