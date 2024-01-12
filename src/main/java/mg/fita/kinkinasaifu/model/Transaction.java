package mg.fita.kinkinasaifu.model;

import mg.fita.kinkinasaifu.annotations.Column;
import mg.fita.kinkinasaifu.annotations.Id;
import mg.fita.kinkinasaifu.annotations.Table;

import java.time.LocalDateTime;

@Table
public class Transaction {
  @Id
  private long id;
  @Column
  private String label;
  @Column
  private Double amount;
  @Column
  private LocalDateTime dateTime;
  @Column
  private String transactionType;
  @Column
  private String sender;
  @Column
  private String receiver;
  @Column
  private Category category;

  public Transaction(
      long id,
      String label,
      Double amount,
      LocalDateTime dateTime,
      String transactionType,
      String sender,
      String receiver,
      Category category) {
    this.id = id;
    this.label = label;
    this.amount = amount;
    this.dateTime = dateTime;
    this.transactionType = transactionType;
    this.sender = sender;
    this.receiver = receiver;
    this.category = category;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Transaction that)) return false;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "id="
        + id
        + ", label='"
        + label
        + '\''
        + ", amount="
        + amount
        + ", dateTime="
        + dateTime
        + ", transactionType='"
        + transactionType
        + '\''
        + ", sender='"
        + sender
        + '\''
        + ", receiver='"
        + receiver
        + '\''
        + ", category="
        + category
        + '}';
  }
}
