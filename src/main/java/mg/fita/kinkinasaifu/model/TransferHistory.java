package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TransferHistory {
    private Integer id;
    private Integer debtorTransactionId;
    private Integer creditorTransactionId;
    private LocalDateTime transferDateTime;
}
