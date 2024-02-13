import java.io.File;
import java.io.IOException;
import java.util.List;

public interface OperacionesJsonInterface {
    List<DatosNoticias> read (File file) throws IOException;
}
