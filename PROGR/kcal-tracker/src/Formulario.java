import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Formulario {

    private final Map<String, String> valores;

    public Formulario(String cuerpo) {
        this.valores = new HashMap<>();
        if (cuerpo == null || cuerpo.trim().isEmpty()) {
            return;
        }

        String[] partes = cuerpo.split("&");
        for (String parte : partes) {
            String[] claveValor = parte.split("=", 2);
            String clave = decodificar(claveValor[0]);
            String valor = claveValor.length > 1 ? decodificar(claveValor[1]) : "";
            valores.put(clave, valor);
        }
    }

    public String texto(String clave) {
        return valores.getOrDefault(clave, "").trim();
    }

    public int entero(String clave) {
        String valor = texto(clave);
        if (valor.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(valor);
    }

    public double decimal(String clave) {
        String valor = texto(clave).replace(',', '.');
        if (valor.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(valor);
    }

    public LocalDate fecha(String clave) {
        String valor = texto(clave);
        if (valor.isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(valor);
    }

    public <T extends Enum<T>> T enumerado(String clave, Class<T> tipo, T valorPorDefecto) {
        String valor = texto(clave);
        if (valor.isEmpty()) {
            return valorPorDefecto;
        }
        return Enum.valueOf(tipo, valor);
    }

    private String decodificar(String valor) {
        try {
            return URLDecoder.decode(valor, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo decodificar el formulario", e);
        }
    }
}
