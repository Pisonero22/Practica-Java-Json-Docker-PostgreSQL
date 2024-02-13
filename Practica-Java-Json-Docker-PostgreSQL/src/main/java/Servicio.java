import Exceptions.AppException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Servicio implements ServicioInterface{
    private Dao dao;
    private OperacionesJson operacionesJson;
    public Servicio(String username, String password) throws SQLException {
        this.dao = new DataBase(username,password);
        this.operacionesJson = new OperacionesJson();
    }
    @Override
    public Periodico registrarPeriodico(String nombre, LocalDate fecha) throws AppException {
        Periodico periodico = dao.registrarPeriodico(nombre, fecha);
        return periodico;
    }

    @Override
    public Noticia registrarNoticia(String id, LocalDate fecha, String titular, String texto, Seccion seccion) throws AppException {
        Noticia noticia = dao.registrarNoticia(id, fecha, titular, texto, seccion);
        return noticia;
    }

    @Override
    public List<Noticia> importarNoticias(String id, File jsonFile) throws AppException, IOException {

        List<DatosNoticias> noticias = operacionesJson.read(jsonFile);
        return dao.importNoticias(id,noticias);
    }

    @Override
    public Optional<Periodico> queryPeriodicoById(String id) throws AppException {
        Optional<Periodico> periodico = dao.queryPeriodicoById(id);
        System.out.println(periodico);
        return periodico;
    }

    @Override
    public List<ResumenPrensa> queryResumenPrensaByFecha(LocalDate fecha) throws AppException {
        List<ResumenPrensa> resumenPrensas = dao.queryResumenPrensaByFecha(fecha);
        resumenPrensas.forEach(System.out::println);
        return resumenPrensas;
    }

    @Override
    public void exportarDatos(File file) throws AppException, IOException {
        List<ResumenPrensa> resumenPrensas = dao.queryResumenPrensa();
        operacionesJson.crearJson(file,resumenPrensas);
    }


    @Override
    public void close() throws Exception {
        dao.close();
    }

}
