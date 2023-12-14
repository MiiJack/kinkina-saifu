package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TransferHistory {
    private Integer id;
    private Integer debtorTransferId;
    private Integer creditorTransferId;
    private LocalDateTime transferDateTime;
}
