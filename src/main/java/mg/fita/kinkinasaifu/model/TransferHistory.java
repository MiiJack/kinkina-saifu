package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TransferHistory {
    private Integer id;
    private Integer debtorCurrencyId;
    private Integer creditorCurrencyId;
    private LocalDateTime transferDateTime;
}
