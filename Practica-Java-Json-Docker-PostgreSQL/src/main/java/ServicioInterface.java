import Exceptions.AppException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServicioInterface extends AutoCloseable{

    Periodico registrarPeriodico(String nombre, LocalDate fecha) throws AppException;

    Noticia registrarNoticia(String id, LocalDate fecha,String titular,String texto,Seccion seccion) throws AppException;
    List<Noticia> importarNoticias(String id, File jsonFile) throws AppException, IOException;

    Optional<Periodico> queryPeriodicoById(String id) throws AppException;

    List<ResumenPrensa> queryResumenPrensaByFecha(LocalDate fecha) throws AppException;

    public void exportarDatos(File file) throws AppException, IOException;

}
