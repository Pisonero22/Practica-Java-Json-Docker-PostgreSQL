import lombok.*;

import java.time.LocalDate;

@Data
@Builder(setterPrefix = "with")
@With
@AllArgsConstructor
@NoArgsConstructor

public class Noticia {
    private String id;
    private String referencia;
    private LocalDate fecha;
    private String titular;
    private String texto;
    private Seccion seccion;
}
