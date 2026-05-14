package ligaesports;

public class PartidoInvalidoException extends Exception { //excepción propia para controlar partidos incorrectos
    public PartidoInvalidoException(String mensaje) { //constructor que recibe el mensaje del error
        super(mensaje); //mando el mensaje a la clase exception
    }
}
