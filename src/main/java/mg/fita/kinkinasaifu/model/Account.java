package mg.fita.kinkinasaifu.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
  private long id;
  private String name;
  private Balance balance;
  private List<Transaction> transactions;
  private Currency currency;
  private String type;

  public Account(
      long id,
      String name,
      Balance balance,
      List<Transaction> transactions,
      Currency currency,
      String type) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.transactions = transactions;
    this.currency = currency;
    this.type = type;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Balance getBalance() {
    return balance;
  }

  public void setBalance(Balance balance) {
    this.balance = balance;
  }

  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account account)) return false;

    return id == account.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Account{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", balance="
        + balance
        + ", transactions="
        + transactions
        + ", currency="
        + currency
        + ", type='"
        + type
        + '\''
        + '}';
  }
}
