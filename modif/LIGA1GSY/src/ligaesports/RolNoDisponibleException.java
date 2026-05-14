package ligaesports;

public class RolNoDisponibleException extends Exception { //excepción propia para avisar de roles no disponibles
    public RolNoDisponibleException(String mensaje) { //constructor que recibe el mensaje del error
        super(mensaje); //mando el mensaje a la clase exception
    }
}
