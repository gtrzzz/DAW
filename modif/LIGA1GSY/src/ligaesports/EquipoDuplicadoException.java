package ligaesports;

public class EquipoDuplicadoException extends Exception { //excepción propia para controlar equipos repetidos
    public EquipoDuplicadoException(String mensaje) { //constructor que recibe el mensaje del error
        super(mensaje); //mando el mensaje a la clase exception
    }
}
