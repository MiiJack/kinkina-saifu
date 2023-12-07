CREATE DATABASE kinkinasaifu;

\c kinkinasaifu

-- Currencies table
CREATE TABLE IF NOT EXISTS "currency" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(3) NOT NULL UNIQUE
);

-- Create the 'account_type & account_name' ENUM type
CREATE TYPE account_name AS ENUM ('Savings', 'Current');
CREATE TYPE account_type AS ENUM ('Bank', 'Cash', 'Mobile Money');

-- Accounts table
CREATE TABLE IF NOT EXISTS "account" (
    id SERIAL PRIMARY KEY,
    name account_name,
    type account_type,
    currency_id INT REFERENCES "currency"(id) ON DELETE SET NULL
);

-- Balance table
CREATE TABLE IF NOT EXISTS "balance" (
    account_id INT REFERENCES "account"(id),
    value DOUBLE PRECISION,
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, modification_date)
);

-- Create the 'transaction_type' ENUM type
CREATE TYPE transaction_type AS ENUM ('Debit', 'Credit');

-- Transactions table
CREATE TABLE IF NOT EXISTS "transaction" (
    id SERIAL PRIMARY KEY,
    label VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type transaction_type,
    sender VARCHAR(255),
    receiver VARCHAR(255)
);

-- Account_Transaction table
CREATE TABLE IF NOT EXISTS "account_transaction" (
    account_id INT REFERENCES "account"(id),
    transaction_id INT REFERENCES "transaction"(id),
    PRIMARY KEY (account_id, transaction_id)
);
