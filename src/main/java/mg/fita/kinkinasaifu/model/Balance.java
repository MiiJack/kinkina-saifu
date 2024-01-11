package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

public class Balance {
  private long id;
  private Double value;
  private LocalDateTime modificationDate;

  public Balance() {}

  public Balance(long id, Double value, LocalDateTime modificationDate) {
    this.id = id;
    this.value = value;
    this.modificationDate = modificationDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public LocalDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(LocalDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Balance balance)) return false;

    return id == balance.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Balance{"
        + "id="
        + id
        + ", value="
        + value
        + ", modificationDate="
        + modificationDate
        + '}';
  }
}
