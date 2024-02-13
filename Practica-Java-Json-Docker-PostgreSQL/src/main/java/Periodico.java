import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Data
@Builder(setterPrefix = "with")
@With
@AllArgsConstructor
@NoArgsConstructor

public class Periodico {
    private String id;
    private String nombre;
    private LocalDate fecha;
}