package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

public class TransactionService {
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();

    public void transferMoney(Account senderAccount, Account receiverAccount, double amount) {
        if (senderAccount.getId() == receiverAccount.getId()) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        // Handle currency conversion if necessary
        if (!senderAccount.getCurrency().getCode().equals(receiverAccount.getCurrency().getCode())) {
            // Get the currency conversion rate for the sender's currency to the receiver's
            // currency
            double conversionRate = otherCrudOperations.getCurrencyValue(senderAccount.getCurrency().getId(),
                    receiverAccount.getCurrency().getId());

            // Convert the amount to the receiver's currency
            amount *= conversionRate;
        }
    }
}