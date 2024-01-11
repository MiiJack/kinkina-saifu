package mg.fita.kinkinasaifu.model;

import java.util.Objects;

public class SumAmount {
  private String category;
  private double totalAmount;

  public SumAmount(String category, double totalAmount) {
    this.category = category;
    this.totalAmount = totalAmount;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SumAmount sumAmount)) return false;

    if (Double.compare(totalAmount, sumAmount.totalAmount) != 0) return false;
    return Objects.equals(category, sumAmount.category);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = category != null ? category.hashCode() : 0;
    temp = Double.doubleToLongBits(totalAmount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
