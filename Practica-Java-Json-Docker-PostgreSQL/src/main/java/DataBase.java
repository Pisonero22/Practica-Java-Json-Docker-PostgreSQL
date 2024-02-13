import Exceptions.*;
import org.postgresql.Driver;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataBase implements Dao{

    private Connection connection;

    public DataBase(String username, String password) throws SQLException {
        DriverManager.registerDriver(new Driver());
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/upsa",username,password);
    }

    @Override
    public Periodico registrarPeriodico(String nombre, LocalDate fecha) throws AppException {

        final String SQL = """
                           INSERT INTO periodicos (id                       ,nombre ,fecha)
                                            VALUES(nextval('seq_periodicos'),?      ,?)                           
                           """;
        String [] columns = {"id","nombre"};
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL,columns)){

            preparedStatement.setString(1,nombre);
            preparedStatement.setDate(2, Date.valueOf(fecha));
            preparedStatement.executeUpdate();
            Periodico periodico = Periodico.builder()
                    .withNombre(nombre)
                    .withFecha(fecha)
                    .build();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                String id = resultSet.getString(1);
                return periodico.withId(id);
            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }
    @Override
    public Noticia registrarNoticia(String id, LocalDate fecha, String titular, String texto, Seccion seccion) throws AppException {

        final String SQL = """
                           INSERT INTO noticias(referencia,id,fecha,titular,texto,seccion)
                                        VALUES (?         ,? ,?    ,?      ,?    ,?      )
                           """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            String referencia = UUID.randomUUID().toString();
            preparedStatement.setString(1,referencia);
            preparedStatement.setString(2,id);
            preparedStatement.setDate(3, Date.valueOf(fecha));
            preparedStatement.setString(4,titular);
            preparedStatement.setString(5,texto);
            preparedStatement.setString(6,seccion.name());
            preparedStatement.executeUpdate();

            return Noticia.builder()
                    .withReferencia(referencia)
                    .withId(id)
                    .withFecha(fecha)
                    .withTitular(titular)
                    .withTexto(texto)
                    .withSeccion(seccion)
                    .build();

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }
    @Override
    public List<Noticia> importNoticias(String id, List<DatosNoticias> datosNoticias) throws AppException {
        final String SQL = """
                            INSERT INTO noticias(referencia,id,fecha,titular,texto,seccion)
                                        VALUES (?          ,? ,?    ,?      ,?    ,?      )
                           """;

        List<Noticia> noticias = new ArrayList<>();



        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            for (DatosNoticias datosNoticia : datosNoticias) {
                String referencia = UUID.randomUUID().toString();
                preparedStatement.setString(1,referencia);
                preparedStatement.setString(2,id);
                preparedStatement.setDate(3, Date.valueOf(datosNoticia.getFecha()));
                preparedStatement.setString(4, datosNoticia.getTitular());
                preparedStatement.setString(5,datosNoticia.getTexto());
                preparedStatement.setString(6,datosNoticia.getSeccion().name());
                preparedStatement.executeUpdate();

                Noticia noticia = Noticia.builder()
                        .withReferencia(referencia)
                        .withId(id)
                        .withFecha(datosNoticia.getFecha())
                        .withTitular(datosNoticia.getTitular())
                        .withTexto(datosNoticia.getTexto())
                        .withSeccion(datosNoticia.getSeccion())
                        .build();
                noticias.add(noticia);
            }
            return noticias;

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

    }
    @Override
    public Optional<Periodico> queryPeriodicoById(String id) throws AppException {

        final  String SQL = """
                            SELECT p.id, p.nombre, p.fecha
                            FROM periodicos p
                            WHERE p.id = ?
                            """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)){

            preparedStatement.setString(1,id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(Periodico.builder()
                            .withId(resultSet.getString(1))
                            .withNombre(resultSet.getString(2))
                            .withFecha(resultSet.getDate(3).toLocalDate())
                            .build());
                }else {
                    System.out.println("No se encuentra el periodico");
                    return Optional.empty();
                }
            }


        }catch (SQLException sqlException){
            throw manager(sqlException);
        }


    }
    @Override
    public List<ResumenPrensa> queryResumenPrensaByFecha(LocalDate fecha) throws AppException {
        final String SQL = """
                            SELECT p.id, p.nombre,p.fecha,n.referencia, n.id,n.fecha,n.titular,n.texto,n.seccion
                                FROM periodicos p
                                JOIN noticias n on p.id = n.id
                            WHERE  n.fecha = ?
                           """;

        List<ResumenPrensa> resumenPrensas = new ArrayList<>();
        String nada ="";
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            preparedStatement.setDate(1, Date.valueOf(fecha));

            try(ResultSet resultSet = preparedStatement.executeQuery()){

                if(resultSet.next()){
                    do{
                        if(!resultSet.getString(1).equals(nada)){
                            ResumenPrensa build = ResumenPrensa.builder()
                                    .withPeriodico(Periodico.builder()
                                            .withId(resultSet.getString(1))
                                            .withNombre(resultSet.getString(2))
                                            .withFecha(resultSet.getDate(3).toLocalDate())
                                            .build())
                                    .withNoticia(Noticia.builder()
                                            .withReferencia(resultSet.getString(4))
                                            .withId(resultSet.getString(5))
                                            .withFecha(LocalDate.parse(resultSet.getString(6)))
                                            .withTitular(resultSet.getString(7))
                                            .withTexto(resultSet.getString(8))
                                            .withSeccion(Seccion.valueOf(resultSet.getString(9)))
                                            .build())
                                    .build();

                            resumenPrensas.add(build);
                            nada=resultSet.getString(2);
                        }else{
                            List<Noticia> noticias = new ArrayList<>();
                            Noticia noticia = Noticia.builder()
                                    .withReferencia(resultSet.getString(4))
                                    .withId(resultSet.getString(5))
                                    .withFecha(LocalDate.parse(resultSet.getString(6)))
                                    .withTitular(resultSet.getString(7))
                                    .withTexto(resultSet.getString(8))
                                    .withSeccion(Seccion.valueOf(resultSet.getString(9)))
                                    .build();
                            noticias.add(noticia);
                            int i=0;
                            for (ResumenPrensa resumenPrensa : resumenPrensas) {
                                if(resumenPrensa.getPeriodico().getNombre().equals(nada)){
                                    noticias.addAll(resumenPrensa.getNoticias());
                                    break;
                                }
                                i++;
                            }
                            noticias.add(noticia);
                            resumenPrensas.get(i).setNoticias(noticias);

                        }




                    }while (resultSet.next());
                }else {
                    System.out.println("No hay ningun noticia");
                    return null;
                }

                return resumenPrensas;

            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

    }
    @Override
    public List<ResumenPrensa> queryResumenPrensa() throws AppException {
        final String SQL = """
                            SELECT p.id, p.nombre,p.fecha,n.referencia, n.id,n.fecha,n.titular,n.texto,n.seccion
                                FROM periodicos p
                                JOIN noticias n on p.id = n.id
                           """;

        List<ResumenPrensa> resumenPrensas = new ArrayList<>();
        String nada ="";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL)
        ){

                if(resultSet.next()){
                    do{
                        if(!resultSet.getString(1).equals(nada)){
                            ResumenPrensa build = ResumenPrensa.builder()
                                    .withPeriodico(Periodico.builder()
                                            .withId(resultSet.getString(1))
                                            .withNombre(resultSet.getString(2))
                                            .withFecha(resultSet.getDate(3).toLocalDate())
                                            .build())
                                    .withNoticia(Noticia.builder()
                                            .withReferencia(resultSet.getString(4))
                                            .withId(resultSet.getString(5))
                                            .withFecha(LocalDate.parse(resultSet.getString(6)))
                                            .withTitular(resultSet.getString(7))
                                            .withTexto(resultSet.getString(8))
                                            .withSeccion(Seccion.valueOf(resultSet.getString(9)))
                                            .build())
                                    .build();

                            resumenPrensas.add(build);
                            nada=resultSet.getString(2);
                        }else{
                            List<Noticia> noticias = new ArrayList<>();
                            Noticia noticia = Noticia.builder()
                                    .withReferencia(resultSet.getString(4))
                                    .withId(resultSet.getString(5))
                                    .withFecha(LocalDate.parse(resultSet.getString(6)))
                                    .withTitular(resultSet.getString(7))
                                    .withTexto(resultSet.getString(8))
                                    .withSeccion(Seccion.valueOf(resultSet.getString(9)))
                                    .build();
                            noticias.add(noticia);
                            int i=0;
                            for (ResumenPrensa resumenPrensa : resumenPrensas) {
                                if(resumenPrensa.getPeriodico().getNombre().equals(nada)){
                                    noticias.addAll(resumenPrensa.getNoticias());
                                    break;
                                }
                                i++;
                            }
                            noticias.add(noticia);
                            resumenPrensas.get(i).setNoticias(noticias);

                        }

                    }while (resultSet.next());
                }else {
                    System.out.println("No hay ningun noticia");
                    return null;
                }

                return resumenPrensas;



        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }
    public AppException manager(SQLException sqlException) throws  AppException{
        String message = sqlException.getMessage();

        if(message.contains("nombre_rep")){ return new NombreRepetido();}
        if(message.contains("periodicos_pk")) return new Periodico_PK();
        if(message.contains("nombre_nn_null")) return new NombreNotNull();
        if(message.contains("fecha_nn_null")) return new FechaPeriodicoNotNull();
        if(message.contains("referencia_pk")) return new ReferenciaPK();
        if(message.contains("id_fk")) return new RegistroIdFk();
        if(message.contains("fecha_nn")) return new FechaNotNull();
        if(message.contains("texto_null")) return new TextoNotNull();
        if(message.contains("titular_nn")) return new TitularNotNull();
        if(message.contains("seccion_nn")) return new SeccionNotNull();
        if(message.contains("SECCION_DISTINTO")) return new SeccionDistinto();
        if(message.contains("id_nn")) return new IdNotNull();


        throw new NonControllatedException(sqlException);
    }
    @Override
    public void close() throws Exception {
        connection.close();
    }

}
