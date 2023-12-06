package mg.fita.kinkinasaifu.model;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Account {
    private int id;
    private String name;
    private Balance balance;
    private Transaction transaction;
    private Currency currency;
    private String type;
}
