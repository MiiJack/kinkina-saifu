package mg.fita.kinkinasaifu.model;

import lombok.*;
import java.util.Currency;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Account {
    private int id;
    private Currency principalCurrency;
    private String name;
}
