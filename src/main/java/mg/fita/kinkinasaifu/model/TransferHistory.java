package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

public class TransferHistory {
  private long id;
  private long debtorTransferId;
  private long creditorTransferId;
  private Double amount;
  private LocalDateTime transferDateTime;

  public TransferHistory() {}

  public TransferHistory(
      long id,
      long debtorTransferId,
      long creditorTransferId,
      Double amount,
      LocalDateTime transferDateTime) {
    this.id = id;
    this.debtorTransferId = debtorTransferId;
    this.creditorTransferId = creditorTransferId;
    this.amount = amount;
    this.transferDateTime = transferDateTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDebtorTransferId() {
    return debtorTransferId;
  }

  public void setDebtorTransferId(long debtorTransferId) {
    this.debtorTransferId = debtorTransferId;
  }

  public long getCreditorTransferId() {
    return creditorTransferId;
  }

  public void setCreditorTransferId(long creditorTransferId) {
    this.creditorTransferId = creditorTransferId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public LocalDateTime getTransferDateTime() {
    return transferDateTime;
  }

  public void setTransferDateTime(LocalDateTime transferDateTime) {
    this.transferDateTime = transferDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TransferHistory that)) return false;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "TransferHistory{"
        + "id="
        + id
        + ", debtorTransferId="
        + debtorTransferId
        + ", creditorTransferId="
        + creditorTransferId
        + ", amount="
        + amount
        + ", transferDateTime="
        + transferDateTime
        + '}';
  }
}
