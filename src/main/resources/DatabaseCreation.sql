Create DATABASE kinkinasaifu;

\c kinkinasaifu

-- Currencies table
CREATE TABLE IF NOT EXISTS "currency" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(3) NOT NULL
);

-- Accounts table
CREATE TABLE IF NOT EXISTS "account" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    principal_currency_id INT REFERENCES currency(id) ON DELETE SET NULL
);

-- Transactions table
CREATE TABLE IF NOT EXISTS "transaction" (
    id SERIAL PRIMARY KEY,
    amount DECIMAL NOT NULL,
    account_id INT REFERENCES account(id) ON DELETE CASCADE,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
