import jakarta.json.JsonArray;
import jakarta.json.JsonValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class NoticiaAdapter implements JsonAdapter{

    List<Noticia> noticias = new ArrayList<>();
    @Override
    public List<DatosNoticias> fromJsonAdapter(JsonArray jsonArray) {
        return JsonAdapter.super.fromJsonAdapter(jsonArray);
    }



    @Override
    public List<DatosNoticias> fromJson(JsonArray jsonArray) {
        List<DatosNoticias> noticias1 = fromJsonAdapter(jsonArray);
        return noticias1;
    }
}
