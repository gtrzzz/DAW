package ligaesports;

public class PersonaDuplicadaException extends Exception { //excepción propia para controlar personas repetidas
    public PersonaDuplicadaException(String mensaje) { //constructor que recibe el mensaje del error
        super(mensaje); //mando el mensaje a la clase exception
    }
}
