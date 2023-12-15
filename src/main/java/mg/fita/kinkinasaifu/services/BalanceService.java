package mg.fita.kinkinasaifu.services;

import mg.fita.kinkinasaifu.model.*;
import mg.fita.kinkinasaifu.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceService {
    BalanceCrudOperations balanceCrudOperations = new BalanceCrudOperations();
    public Balance getBalanceAtDateTime(Account account, LocalDateTime dateTime) {
        List<Balance> balances = balanceCrudOperations.findAllByAccountId(account.getId());
        for (int i = 0; i < balances.size(); i++) {
            if (balances.get(i).getModificationDate().isAfter(dateTime)) {
                return balances.get(i - 1);
            }
        }
        return balances.get(balances.size() - 1);
    }

    public List<Balance> getBalanceHistory(Account account, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Balance> balances = balanceCrudOperations.findAllByAccountId(account.getId());

        return balances.stream()
                .filter(balance -> balance.getModificationDate().isAfter(startDateTime) && balance.getModificationDate().isBefore(endDateTime))
                .collect(Collectors.toList());
    }


}
