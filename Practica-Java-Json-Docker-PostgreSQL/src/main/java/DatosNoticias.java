import lombok.*;

import java.time.LocalDate;



@Data
@Builder(setterPrefix = "with")
@With
@AllArgsConstructor
@NoArgsConstructor
public class DatosNoticias {

    private LocalDate fecha;
    private Seccion seccion;
    private String titular;
    private String texto;
}
