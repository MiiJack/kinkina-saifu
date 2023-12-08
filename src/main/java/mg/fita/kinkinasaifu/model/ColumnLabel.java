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
    }

    public class AccountTransactionTable {
        public static final String ACCOUNT_ID = "account_id";
        public static final String TRANSACTION_ID = "transaction_id";
    }

}
