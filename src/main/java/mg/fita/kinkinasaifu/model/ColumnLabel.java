package mg.fita.kinkinasaifu.model;

public class ColumnLabel {
    public class CurrencyTable{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CODE = "code";
    }

    public class AccountTable {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String CURRENCY_ID = "currency_id";
    }

    public class BalanceTable {
        public static final String ACCOUNT_ID = "account_id";
        public static final String VALUE = "value";
        public static final String MODIFICATION_DATE = "modification_date";
    }

    public class TransactionTable {
        public static final String ID = "id";
        public static final String LABEL = "label";
        public static final String AMOUNT = "amount";
        public static final String DATE_TIME = "date_time";
        public static final String TYPE = "type";
        public static final String SENDER = "sender";
        public static final String RECEIVER = "receiver";
        public static final String CATEGORY = "category";
    }

    public class AccountTransactionTable {
        public static final String ACCOUNT_ID = "account_id";
        public static final String TRANSACTION_ID = "transaction_id";
    }

    public class TransferHistoryTable {
        public static final String ID = "id";
        public static final String DEBTOR_TRANSACTION_ID = "debtor_transaction_id";
        public static final String CREDITOR_TRANSACTION_ID = "creditor_transaction_id";
        public static final String AMOUNT ="amount";
        public static final String TRANSFER_DATE_TIME = "transfer_date_time";

    }

    public class CurrencyValueTable{
        public static final String ID = "id";
        public static final String SOURCE_CURRENCY_ID = "source_currency_id";
        public static final String TARGET_CURRENCY_ID = "target_currency_id";
        public static final String VALUE = "value";
        public static final String EFFECTIVE_DATE = "effective_date";

    }

}
