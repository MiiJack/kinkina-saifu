package mg.fita.kinkinasaifu.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int id;
    private String label;
    private Double amount;
    private LocalDateTime dateTime;
    private String transactionType;
    private String sender;
    private String receiver;
    private Category category;
}