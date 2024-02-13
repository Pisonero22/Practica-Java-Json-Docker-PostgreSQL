import Exceptions.AppException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Dao extends AutoCloseable{

    Periodico registrarPeriodico(String nombre, LocalDate fecha) throws AppException;
    Noticia registrarNoticia(String id, LocalDate fecha,String titular,String texto,Seccion seccion) throws AppException;

    List<Noticia> importNoticias(String id, List<DatosNoticias> datosNoticias) throws AppException;

    Optional<Periodico> queryPeriodicoById(String id) throws AppException;

    List<ResumenPrensa> queryResumenPrensaByFecha(LocalDate fecha) throws AppException;
    List<ResumenPrensa> queryResumenPrensa() throws AppException;
}
