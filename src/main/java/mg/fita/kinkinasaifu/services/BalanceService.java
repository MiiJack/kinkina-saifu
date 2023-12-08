package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

public class BalanceService {
    OtherCrudOperations otherCrudOperations = new OtherCrudOperations();
    public Balance getBalanceAtDateTime(Account account, LocalDateTime dateTime) {

        // Get the list of balances sorted by modification date
        List<Balance> balances = otherCrudOperations.findAllByAccountId(account.getId());

        // Find the balance immediately before the given date and time
        for (int i = 0; i < balances.size(); i++) {
            if (balances.get(i).getModificationDate().isAfter(dateTime)) {
                return balances.get(i - 1);
            }
        }

        // If the given date and time is after all balance modification dates, return the latest balance
        return balances.get(balances.size() - 1);
    }
}
