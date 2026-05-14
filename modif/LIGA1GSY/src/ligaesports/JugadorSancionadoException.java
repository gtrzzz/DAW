package ligaesports;

public class JugadorSancionadoException extends Exception { //excepción propia para avisar de jugadores sancionados
    public JugadorSancionadoException(String mensaje) { //constructor que recibe el mensaje del error
        super(mensaje); //mando el mensaje a la clase exception
    }
}
