package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

public class CurrencyValue {
  private long id;
  private Integer sourceCurrencyId;
  private Integer destinationCurrencyId;
  private Double amount;
  private LocalDateTime transferDateTime;

  public CurrencyValue(
      long id,
      Integer sourceCurrencyId,
      Integer destinationCurrencyId,
      Double amount,
      LocalDateTime transferDateTime) {
    this.id = id;
    this.sourceCurrencyId = sourceCurrencyId;
    this.destinationCurrencyId = destinationCurrencyId;
    this.amount = amount;
    this.transferDateTime = transferDateTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Integer getSourceCurrencyId() {
    return sourceCurrencyId;
  }

  public void setSourceCurrencyId(Integer sourceCurrencyId) {
    this.sourceCurrencyId = sourceCurrencyId;
  }

  public Integer getDestinationCurrencyId() {
    return destinationCurrencyId;
  }

  public void setDestinationCurrencyId(Integer destinationCurrencyId) {
    this.destinationCurrencyId = destinationCurrencyId;
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
    if (!(o instanceof CurrencyValue that)) return false;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "CurrencyValue{"
        + "id="
        + id
        + ", sourceCurrencyId="
        + sourceCurrencyId
        + ", destinationCurrencyId="
        + destinationCurrencyId
        + ", amount="
        + amount
        + ", transferDateTime="
        + transferDateTime
        + '}';
  }
}
