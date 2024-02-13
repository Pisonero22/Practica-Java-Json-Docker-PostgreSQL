import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OperacionesJson implements OperacionesJsonInterface{

    private NoticiaAdapter noticiaAdapter ;

    public OperacionesJson() {
        this.noticiaAdapter = new NoticiaAdapter();
    }


    @Override
    public List<DatosNoticias> read(File file) throws IOException {
        List<DatosNoticias> noticias = new ArrayList<>();
        try(FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            JsonReader jsonReader = Json.createReader(br)
        ){
            noticias= noticiaAdapter.fromJson(jsonReader.readArray());

        }
        return noticias;
    }

    public String toJson(List<ResumenPrensa> resumenPrensas){
        Jsonb jsonb = JsonbBuilder.newBuilder().build();
        return jsonb.toJson(resumenPrensas);

    }
    public void crearJson(File file,List<ResumenPrensa> prensas) throws IOException {

        try(FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw)
        ){
            pw.write(toJson(prensas));
            pw.flush();
        }

    }
}
