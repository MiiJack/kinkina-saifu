package mg.fita.kinkinasaifu.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {
    private int id;
    private Account account;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}
