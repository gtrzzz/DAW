package ligaesports;

public class RolNoDisponibleException extends Exception { //como el resto de excepciones (explicado en PersonaDuplicadaException)
    public RolNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}