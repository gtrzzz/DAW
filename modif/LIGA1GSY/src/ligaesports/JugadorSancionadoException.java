package ligaesports;

public class JugadorSancionadoException extends Exception { //como el resto de excepciones (explicado en PersonaDuplicadaException)
    public JugadorSancionadoException(String mensaje) {
        super(mensaje);
    }
}