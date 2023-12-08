package mg.fita.kinkinasaifu.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistory {
    private int id;
    private int debtorTransactionId;
    private int creditorTransactionId;
    private LocalDateTime transferDateTime;

}


