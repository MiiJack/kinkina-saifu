package mg.fita.kinkinasaifu.model;

public class Currency {
  private long id;
  private String name;
  private String code;

  public Currency(long id, String name, String code) {
    this.id = id;
    this.name = name;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Currency currency)) return false;

    return id == currency.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Currency{" + "id=" + id + ", name='" + name + '\'' + ", code='" + code + '\'' + '}';
  }
}
