package mg.fita.kinkinasaifu.model;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private String name;
    private Balance balance;
    private List<Transaction> transactions;
    private Currency currency;
    private String type;

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}