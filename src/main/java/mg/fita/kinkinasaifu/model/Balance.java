package mg.fita.kinkinasaifu.model;

import java.time.LocalDateTime;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
  private Double value;
  private LocalDateTime modificationDate;
}
