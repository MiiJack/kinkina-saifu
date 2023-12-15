package mg.fita.kinkinasaifu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
