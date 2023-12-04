package mg.fita.kinkinasaifu.model;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Account {
    private int id;
    private Currency principalCurrency;
    private String name;
}
