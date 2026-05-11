package ligaesports;

public class EquipoDuplicadoException extends Exception { //como el resto de excepciones (explicado en PersonaDuplicadaException)
    public EquipoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}