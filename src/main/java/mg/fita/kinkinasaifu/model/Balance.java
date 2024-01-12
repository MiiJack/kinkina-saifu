package mg.fita.kinkinasaifu.model;

import mg.fita.kinkinasaifu.annotations.Column;
import mg.fita.kinkinasaifu.annotations.Id;
import mg.fita.kinkinasaifu.annotations.Table;

import java.time.LocalDateTime;

@Table
public class Balance {
  @Id
  private long id;
  @Column
  private Double value;
  @Column
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
