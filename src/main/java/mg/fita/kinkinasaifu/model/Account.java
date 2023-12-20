package mg.fita.kinkinasaifu.model;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

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

  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public Category getCategory() {
    return null;
  }
}
