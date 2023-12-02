-- Mock data for currencies
INSERT INTO currencies (name, code) VALUES
('United States Dollar', 'USD'),
('Euro', 'EUR'),
('British Pound', 'GBP'),
('Japanese Yen', 'JPY'),
('Swiss Franc', 'CHF');

-- Mock data for accounts
INSERT INTO accounts (name, principal_currency_id) VALUES
('Andy', 1),
('Don Smokilla', 2),
('Charlie Brown', 3),
('Deil Delta', 4),
('Everbridge Vitas', 5);

-- Mock data for transactions
INSERT INTO transactions (amount, account_id, transaction_date) VALUES
(100.00, 1, '2023-01-01 00:00:00'),
(200.00, 2, '2023-02-01 00:00:00'),
(300.00, 3, '2023-03-01 00:00:00'),
(400.00, 4, '2023-04-01 00:00:00'),
(500.00, 5, '2023-05-01 00:00:00');
