package ligaesports;

public class PersonaDuplicadaException extends Exception { //crea excepción propia que hereda de exception
    public PersonaDuplicadaException(String mensaje) { //constructor con parámetro de entrada de String  
        super(mensaje); 
    }
}