import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("noticias.json");
        File file2 = new File("resumenPrensa.json");

        try(Servicio servicio = new Servicio("system","manager")){
            servicio.registrarPeriodico("El Mundo", LocalDate.now());
            servicio.registrarPeriodico("ABC", LocalDate.now());
            servicio.registrarPeriodico("LA Razon", LocalDate.now());

            servicio.registrarNoticia("0",LocalDate.now(),"Examen Json","Examen en la UPSA",Seccion.INTERNACIONAL);
            servicio.registrarNoticia("0",LocalDate.now(),"Examen Json","Examen en la UPSA",Seccion.CULTURA);
            servicio.registrarNoticia("1",LocalDate.now(),"Examen Java","Examen en la UPSA",Seccion.ECONOMIA);
            servicio.registrarNoticia("1",LocalDate.now(),"Examen Java","Examen en la UPSA",Seccion.NACIONAL);
            servicio.registrarNoticia("2",LocalDate.now(),"Examen SQL","Examen en la UPSA",Seccion.DEPORTES);
            servicio.registrarNoticia("2",LocalDate.now(),"Examen SQL","Examen en la UPSA",Seccion.INTERNACIONAL);

            servicio.importarNoticias("0",file);

            servicio.queryPeriodicoById("0");

            servicio.queryResumenPrensaByFecha(LocalDate.now());

            servicio.exportarDatos(file2);


        }


    }
}
