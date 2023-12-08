package mg.fita.kinkinasaifu.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    private Double value;
    private LocalDateTime modificationDate;
}
