
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public interface JsonAdapter {


     default List<DatosNoticias> fromJsonAdapter(JsonArray jsonArray){
        List<DatosNoticias> noticias = new ArrayList<>();
        for (JsonValue jsonValue : jsonArray) {
            DatosNoticias noticia = DatosNoticias.builder()
                    .withTitular(jsonValue.asJsonObject().getString("titular"))
                    .withFecha(LocalDate.parse(jsonValue.asJsonObject().getString("fecha")))
                    .withTexto(jsonValue.asJsonObject().getString("texto"))
                    .withSeccion( Seccion.valueOf(jsonValue.asJsonObject().getString("seccion")))
                    .build();
            noticias.add(noticia);
        }
        return noticias;

    }


    List<DatosNoticias> fromJson(JsonArray jsonArray);

}




