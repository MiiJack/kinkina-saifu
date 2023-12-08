package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TransferHistory {
    private Integer id;
    private Integer debitorCurrrencyId;
    private Integer creditorCurrencyId;
    private LocalDateTime transferDate;
}
