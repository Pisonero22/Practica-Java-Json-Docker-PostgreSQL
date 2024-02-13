import lombok.*;

import java.sql.ClientInfoStatus;
import java.util.List;



@Data
@Builder(setterPrefix = "with")
@With
@AllArgsConstructor
@NoArgsConstructor
public class ResumenPrensa {
    private Periodico periodico;
    @Singular
    private List<Noticia> noticias;

}
