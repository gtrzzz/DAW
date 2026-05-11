package ligaesports;

public class PartidoInvalidoException extends Exception { //como el resto de excepciones (explicado en PersonaDuplicadaException)
    public PartidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}