package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyValue {
  private int id;
  private Integer sourceCurrencyId;
  private Integer destinationCurrencyId;
  private Double amount;
  private LocalDateTime transferDateTime;
}
